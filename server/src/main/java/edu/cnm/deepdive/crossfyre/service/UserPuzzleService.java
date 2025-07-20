package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Guess;
import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleRepository;
import edu.cnm.deepdive.crossfyre.service.dao.UserPuzzleRepository;
import edu.cnm.deepdive.crossfyre.service.dao.UserRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("service")
public class UserPuzzleService implements AbstractUserPuzzleService {

  private final UserPuzzleRepository userPuzzleRepository;
  private final PuzzleRepository puzzleRepository;
  private final UserRepository userRepository;

  @Autowired
  UserPuzzleService(UserPuzzleRepository userPuzzleRepository, PuzzleRepository puzzleRepository,
      UserRepository userRepository) {
    this.userPuzzleRepository = userPuzzleRepository;
    this.puzzleRepository = puzzleRepository;
    this.userRepository = userRepository;
  }

  @Override
  public UserPuzzle get(UUID userPuzzleKey) {
    return userPuzzleRepository
        .findByExternalKey(userPuzzleKey)
        .orElseThrow();
  }

  @Override
  public Iterable<UserPuzzle> getAllByPuzzle(UUID puzzleKey) {
    return puzzleRepository
        .findByExternalKey(puzzleKey)
        .map(userPuzzleRepository::findByPuzzleOrderByCreatedDesc)
        .orElseThrow();
  }

  @Override
  public Iterable<UserPuzzle> getAllByPuzzleDate(Instant date) {
    return puzzleRepository
        .findByDate(date)
        .map(userPuzzleRepository::findByPuzzleOrderByCreatedDesc)
        .orElseThrow();
  }

  @Override
  public Iterable<UserPuzzle> getAllByUser(User user) {
    return userRepository
        .findById(user.getId())
        .map(userPuzzleRepository::findByUserOrderByCreatedDesc)
        .orElseThrow();
  }

  @Override
  public UserPuzzle getOrAddUserPuzzle(User user, Instant date) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(user, date)
        // TODO: 7/20/25 Check with Nick/Reed to make sure this construction works as intended
        .or(() -> Optional.of(
            puzzleRepository
            .findByDate(date)
            .map((puzzle) -> {
              UserPuzzle userPuzzle = new UserPuzzle();
              userPuzzle.setUser(user);
              userPuzzle.setPuzzle(puzzle);
              userPuzzle.setGuesses(new ArrayList<>());
              return userPuzzleRepository.save(userPuzzle);
            })
            .orElseThrow()
        ))
        .orElseThrow();
  }

  @Override
  public UserPuzzle get(User user, Instant date) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(user, date)
        .orElseThrow();
  }


  // TODO: 7/20/25 Add checking logic to determine solved state of both existing and delta (?)
  @Override
  public UserPuzzle updateUserPuzzle(UserPuzzle existing, UserPuzzle delta) {
    // DONE: 7/20/25 Update the UserPuzzle state as such:
    //  1. Bring in changes from delta (HTTP)
    //    - client passes in a guess via HTTP Post
    //    - guess is added to guess table
    //    - need to access (retrieve) up-to-date list of guesses
    //  2. Update guesses field (list of guesses) in current UserPuzzle (game state)
    //  3. Run checking logic (comparison of two grids)
    //  4. Update solved and finished (time) as necessary
    return userPuzzleRepository
        .findById(existing.getId())
        .map((retrieved) -> {
//          retrieved.setSolved(delta.isSolved());
          if (delta.getGuesses() != null) {
            retrieved.setGuesses(delta.getGuesses());
          }

          // DONE: 7/20/25 Check if puzzle is solved
          List<Guess> guesses = retrieved.getGuesses();
          boolean isSolved = checkGuesses(guesses, retrieved.getPuzzle());
          if (isSolved) {
            delta.setFinished(Instant.now());
            delta.setSolved(true);
            retrieved.setFinished(delta.getFinished());
            retrieved.setSolved(delta.isSolved());
          }
          return userPuzzleRepository.save(retrieved);
        })
        .orElseThrow();
  }

  private boolean checkGuesses(List<Guess> guesses, Puzzle solution) {

    // 1. Get list of guesses, create 5x5 array, and load into array
    //    - initialize board to zeros
    //    - fill in guesses, one after another
    int boardLength = 5;
    char[][] userBoard = new char[boardLength][boardLength];
    for (int i = 0; i < boardLength; i++) {
      for (int j = 0; j < boardLength; j++) {
         userBoard[i][j] = '0';
      }
    }
    for (Guess guess : guesses) {
      userBoard[guess.getGuessRow()][guess.getGuessColumn()] = guess.getGuessChar();
    }
    // 2. Use puzzle's word list to generate solution array on the fly
    //     - create empty char array --> char[][] solutionBoard = new char[5][5];
    //     - initialize the board to '0' characters
    char[][] solutionBoard = new char[boardLength][boardLength];
    for (int i = 0; i < boardLength; i++) {
      for (int j = 0; j < boardLength; j++) {
        solutionBoard[i][j] = '0';
      }
    }
    List<PuzzleWord> words = solution.getPuzzleWords();
    for (PuzzleWord word : words) {
      int row = word.getWordRow();
      int col = word.getWordColumn();
      String direction = word.getWordDirection();
      int wordLength = word.getWordLength();
      for (int i = 0; i < wordLength; i++) {
        if (direction.equals("ACROSS")) {
          solutionBoard[row][col + i] = word.getWordName().charAt(i);
        } else {
          solutionBoard[row + i][col] = word.getWordName().charAt(i);
        }
      }
    }
    //     - now you have solution board
    // 3. finally, iterate through both boards and compare letter by letter:
    boolean bSolved = true;
    for (int i = 0; i < boardLength; i++) {
      for (int j = 0; j < boardLength; j++) {
        if (userBoard[i][j] != solutionBoard[i][j]) {
          bSolved = false;
        }
      }
    }
    return bSolved;
  }

}
