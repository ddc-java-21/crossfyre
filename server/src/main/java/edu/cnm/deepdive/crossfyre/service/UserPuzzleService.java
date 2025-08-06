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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing {@link UserPuzzle} entities and their related operations.
 * <p>
 * Provides methods to retrieve, create, update, and validate user puzzles and guesses.
 * </p>
 */
@Service
@Profile("service")
public class UserPuzzleService implements AbstractUserPuzzleService {

  private final UserPuzzleRepository userPuzzleRepository;
  private final PuzzleRepository puzzleRepository;
  private final UserRepository userRepository;
  private final GuessRepository guessRepository;

  @PersistenceContext
  private EntityManager entityManager;

  /**
   * Constructs a {@code UserPuzzleService} with required repositories.
   *
   * @param userPuzzleRepository repository for user puzzles
   * @param puzzleRepository     repository for puzzles
   * @param userRepository       repository for users
   * @param guessRepository      repository for guesses
   */
  @Autowired
  UserPuzzleService(UserPuzzleRepository userPuzzleRepository, PuzzleRepository puzzleRepository,
      UserRepository userRepository, GuessRepository guessRepository) {
    this.userPuzzleRepository = userPuzzleRepository;
    this.puzzleRepository = puzzleRepository;
    this.userRepository = userRepository;
    this.guessRepository = guessRepository;
  }

  /**
   * Retrieves a {@link UserPuzzle} by its external UUID key.
   *
   * @param userPuzzleKey the UUID key of the user puzzle
   * @return the matching {@code UserPuzzle}
   * @throws java.util.NoSuchElementException if no matching user puzzle is found
   */
  @Override
  public UserPuzzle get(UUID userPuzzleKey) {
    return userPuzzleRepository
        .findByExternalKey(userPuzzleKey)
        .orElseThrow();
  }

  /**
   * Retrieves all {@code UserPuzzle} instances associated with a specific puzzle,
   * identified by the puzzle's UUID key, ordered by creation time descending.
   *
   * @param puzzleKey the UUID key of the puzzle
   * @return an iterable of user puzzles associated with the puzzle
   * @throws java.util.NoSuchElementException if the puzzle does not exist
   */
  @Override
  public Iterable<UserPuzzle> getAllByPuzzle(UUID puzzleKey) {
    return puzzleRepository
        .findByExternalKey(puzzleKey)
        .map(userPuzzleRepository::findByPuzzleOrderByCreatedDesc)
        .orElseThrow();
  }

  /**
   * Retrieves all {@code UserPuzzle} instances associated with a puzzle on a specific date,
   * ordered by creation time descending.
   *
   * @param date the date of the puzzle
   * @return an iterable of user puzzles associated with the puzzle on the given date
   * @throws java.util.NoSuchElementException if no puzzle exists for the given date
   */
  @Override
  public Iterable<UserPuzzle> getAllByPuzzleDate(LocalDate date) {
    return puzzleRepository
        .findByDate(date)
        .map(userPuzzleRepository::findByPuzzleOrderByCreatedDesc)
        .orElseThrow();
  }

  /**
   * Retrieves all {@code UserPuzzle} instances associated with a given user,
   * ordered by creation time descending.
   *
   * @param user the user whose puzzles are requested
   * @return an iterable of user puzzles for the user
   * @throws java.util.NoSuchElementException if the user does not exist
   */
  @Override
  public Iterable<UserPuzzle> getAllByUser(User user) {
    return userRepository
        .findById(user.getId())
        .map(userPuzzleRepository::findByUserOrderByCreatedDesc)
        .orElseThrow();
  }

  /**
   * Retrieves an existing {@code UserPuzzle} for the user and puzzle date or creates
   * a new one if it doesn't exist. Checks and updates the solved status based on guesses.
   *
   * @param user   the user
   * @param puzzle the puzzle
   * @return the existing or newly created user puzzle
   * @throws java.util.NoSuchElementException if the puzzle or user puzzle cannot be found or created
   */
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
          // if nothing has changed, return the unchanged user puzzle
          return retrieved;
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

  /**
   * Adds a new {@link Guess} to the {@code UserPuzzle} identified by user and puzzle date.
   * Removes any duplicate guesses on the same coordinates, then updates the solved status.
   *
   * @param requestor  the user making the guess
   * @param puzzleDate the date of the puzzle
   * @param guess      the guess to add
   * @return the updated user puzzle after adding the guess
   * @throws java.util.NoSuchElementException if the user puzzle cannot be found
   */
  @Override
  @Transactional
  public UserPuzzle add(User requestor, LocalDate puzzleDate, Guess guess) {
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

          guess.setUserPuzzle(retrieved);
          retrieved.getGuesses().add(guess);

          List<Guess> guesses = retrieved.getGuesses();
          boolean isSolved = checkGuesses(guesses, retrieved.getPuzzle());
          if (isSolved) {
            retrieved.setSolved(Instant.now());
            retrieved.setSolved(true);
            return userPuzzleRepository.save(retrieved);
          }

          return retrieved;
        })
        .orElseThrow(); // consider throwing a custom exception here
  }

  /**
   * Retrieves a {@code UserPuzzle} for a user and a puzzle date.
   *
   * @param user the user
   * @param date the date of the puzzle
   * @return the matching user puzzle
   * @throws java.util.NoSuchElementException if the user puzzle is not found
   */
  @Override
  public UserPuzzle get(User user, LocalDate date) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(user, date)
        .orElseThrow();
  }

  /**
   * Checks if the provided guesses completely solve the puzzle.
   * Compares user guesses with the puzzle's solution grid.
   *
   * @param guesses  list of guesses made by the user
   * @param solution the puzzle solution to verify against
   * @return {@code true} if all guesses match the solution; {@code false} otherwise
   */
  private boolean checkGuesses(List<Guess> guesses, Puzzle solution) {
    int boardLength = solution.getSize();
    char[][] userBoard = new char[boardLength][boardLength];
    char[][] solutionBoard = new char[boardLength][boardLength];
    boolean bSolved;

    // Initialize boards with '0' as placeholder
    for (int i = 0; i < boardLength; i++) {
      for (int j = 0; j < boardLength; j++) {
        userBoard[i][j] = '0';
        solutionBoard[i][j] = '0';
      }
    }

    // Populate userBoard with guesses
    for (Guess guess : guesses) {
      userBoard[guess.getGuessPosition().guessRow()][guess.getGuessPosition().guessColumn()] = guess.getGuessChar();
    }

    // Populate solutionBoard with puzzle words
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

    // Compare both boards
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
