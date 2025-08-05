package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Guess;
import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import edu.cnm.deepdive.crossfyre.service.dao.GuessRepository;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleRepository;
import edu.cnm.deepdive.crossfyre.service.dao.UserPuzzleRepository;
import edu.cnm.deepdive.crossfyre.service.dao.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Profile("service")
public class UserPuzzleService implements AbstractUserPuzzleService {

  private final UserPuzzleRepository userPuzzleRepository;
  private final PuzzleRepository puzzleRepository;
  private final UserRepository userRepository;
  private final GuessRepository guessRepository;

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  UserPuzzleService(UserPuzzleRepository userPuzzleRepository, PuzzleRepository puzzleRepository,
      UserRepository userRepository, GuessRepository guessRepository) {
    this.userPuzzleRepository = userPuzzleRepository;
    this.puzzleRepository = puzzleRepository;
    this.userRepository = userRepository;
    this.guessRepository = guessRepository;
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
  public UserPuzzle getOrAddUserPuzzle(User user, Puzzle puzzle) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(user, puzzle.getDate())
        .map((retrieved) -> {
          List<Guess> guesses = retrieved.getGuesses();
          boolean isSolved = checkGuesses(guesses, retrieved.getPuzzle());
          if (isSolved) {
            retrieved.setSolved(Instant.now());
            retrieved.setSolved(true);
            // only update the userPuzzle record if the state has changed
            return userPuzzleRepository.save(retrieved);
          }
          retrieved.setSolved(false);
          return userPuzzleRepository.save(retrieved);
          // if nothing has changed, return the unchanged user puzzle
//          return retrieved;
        })
        .or(() -> {
          UserPuzzle userPuzzle = new UserPuzzle();
          userPuzzle.setUser(user);
          userPuzzle.setPuzzle(puzzle);
          userPuzzle.setGuesses(new ArrayList<>());
          userPuzzle.setSolved(false);
          userPuzzleRepository.save(userPuzzle);
          return Optional.of(userPuzzle);
        })
        .orElseThrow();
  }


  @Override
  @Transactional
  public UserPuzzle add(User requestor, Instant puzzleDate, Guess guess) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(requestor, puzzleDate)
        .map((retrieved) -> {
          List<Guess> duplicates = new ArrayList<>();
          for (Guess previousGuess : retrieved.getGuesses()) {
            if (previousGuess.getGuessPosition().guessRow() == guess.getGuessPosition().guessRow()
                && previousGuess.getGuessPosition().guessColumn() == guess.getGuessPosition().guessColumn()) {
              previousGuess.setUserPuzzle(null);
              duplicates.add(previousGuess);
            }
          }
          retrieved.getGuesses().removeAll(duplicates);
          guess.setUserPuzzle((retrieved));
          retrieved.getGuesses().add(guess);
//          userPuzzleRepository.save(retrieved);
          return retrieved;
        })
        .orElseThrow(); // custom exception goes here
  }


  @Override
  public UserPuzzle get(User user, Instant date) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(user, date)
        .orElseThrow();
  }

  private boolean checkGuesses(List<Guess> guesses, Puzzle solution) {
    // Create and initialize comparison grids
    int boardLength = solution.getSize();
    char[][] userBoard = new char[boardLength][boardLength];
    char[][] solutionBoard = new char[boardLength][boardLength];
    boolean bSolved;
    for (int i = 0; i < boardLength; i++) {
      for (int j = 0; j < boardLength; j++) {
         userBoard[i][j] = '0';
         solutionBoard[i][j] = '0';
      }
    }
    // Get a list of guesses, create 5x5 array, and load into an array
    for (Guess guess : guesses) {
      userBoard[guess.getGuessPosition().guessRow()][guess.getGuessPosition().guessColumn()] = guess.getGuessChar();
    }
    // Use puzzle's word list to generate the solution array
    List<PuzzleWord> words = solution.getPuzzleWords();
    for (PuzzleWord word : words) {
      int row = word.getWordPosition().wordRow();
      int col = word.getWordPosition().wordColumn();
      String direction = word.getWordDirection().toString();
      int wordLength = word.getWordPosition().wordLength();
      for (int i = 0; i < wordLength; i++) {
        if (direction.equals("ACROSS")) {
          solutionBoard[row][col + i] = word.getWordName().charAt(i);
        } else {
          solutionBoard[row + i][col] = word.getWordName().charAt(i);
        }
      }
    }
    // Finally, iterate through both boards and compare letter by letter
    bSolved = true;
    for (int i = 0; i < boardLength; i++) {
      for (int j = 0; j < boardLength; j++) {
        if (userBoard[i][j] != solutionBoard[i][j]) {
          bSolved = false;
        }
      }
    }
    System.out.println("Solved: " + bSolved);
    return bSolved;
  }

}
