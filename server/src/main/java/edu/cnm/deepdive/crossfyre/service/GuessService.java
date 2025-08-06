package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Guess;
import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import edu.cnm.deepdive.crossfyre.service.dao.GuessRepository;
import edu.cnm.deepdive.crossfyre.service.dao.UserPuzzleRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing {@link Guess} entities.
 * <p>
 * Provides methods to retrieve guesses by user and puzzle date, retrieve all guesses in a user puzzle,
 * and add a new guess to a user puzzle with transactional support.
 * </p>
 */
@Service
@Profile("service")
public class GuessService implements AbstractGuessService {

  private final GuessRepository guessRepository;
  private final UserPuzzleRepository userPuzzleRepository;

  /**
   * Constructs a {@code GuessService} with the specified repositories.
   *
   * @param guessRepository repository for accessing guesses
   * @param userPuzzleRepository repository for accessing user puzzle associations
   */
  @Autowired
  GuessService(GuessRepository guessRepository, UserPuzzleRepository userPuzzleRepository) {
    this.guessRepository = guessRepository;
    this.userPuzzleRepository = userPuzzleRepository;
  }

  /**
   * Retrieves a specific guess made by the given user for the puzzle on the specified date,
   * identified by the guess's external UUID key.
   *
   * @param user the user who made the guess
   * @param date the date of the puzzle
   * @param guessKey the external UUID key identifying the guess
   * @return the matching {@code Guess}
   * @throws java.util.NoSuchElementException if no matching guess is found
   */
  @Override
  public Guess get(User user, LocalDate date, UUID guessKey) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(user, date)
        .flatMap((userPuzzle) -> guessRepository.findByUserPuzzleAndExternalKey(userPuzzle, guessKey))
        .orElseThrow();
  }

  /**
   * Retrieves all guesses made by the specified user for the puzzle on the given date,
   * ordered by their database ID in ascending order.
   *
   * @param requestor the user whose guesses to retrieve
   * @param puzzleDate the date of the puzzle
   * @return an iterable of all guesses made by the user for the puzzle
   * @throws java.util.NoSuchElementException if the user puzzle association is not found
   */
  @Override
  public Iterable<Guess> getAllInUserPuzzle(User requestor, LocalDate puzzleDate) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(requestor, puzzleDate)
        .map(guessRepository::findByUserPuzzleOrderByIdAsc)
        .orElseThrow();
  }

  /**
   * Adds a new guess to the user puzzle identified by the user and puzzle date.
   * If there is a previous guess at the same position, it is deleted before adding the new guess.
   * This operation is transactional.
   *
   * @param requestor the user making the guess
   * @param puzzleDate the date of the puzzle
   * @param guess the guess to add
   * @return the updated {@code UserPuzzle} entity after adding the guess
   * @throws java.util.NoSuchElementException if the user puzzle association is not found
   */
  @Override
  @Transactional
  public UserPuzzle add(User requestor, LocalDate puzzleDate, Guess guess) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(requestor, puzzleDate)
        .flatMap((userPuzzle) -> {
          for (Guess previousGuess : userPuzzle.getGuesses()) {
            if (previousGuess.getGuessPosition().guessRow() == guess.getGuessPosition().guessRow()
                && previousGuess.getGuessPosition().guessColumn() == guess.getGuessPosition().guessColumn()) {
              guessRepository.delete(previousGuess);
            }
          }
          //userPuzzle.getGuesses().add(guess);
          userPuzzleRepository.save(userPuzzle);
          return userPuzzleRepository.findByUserAndPuzzleDate(requestor, puzzleDate);
        })
        .orElseThrow(); // custom exception goes here
  }
}
