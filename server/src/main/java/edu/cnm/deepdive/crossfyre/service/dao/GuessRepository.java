package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.Guess;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for {@link Guess} entity, providing CRUD operations and custom
 * queries for guesses associated with a {@link UserPuzzle}.
 */
public interface GuessRepository extends CrudRepository<Guess, Long> {

  /**
   * Retrieves a {@link Guess} by its external UUID key.
   *
   * @param externalKey UUID of the guess.
   * @return Optional containing the {@link Guess}, if found.
   */
  Optional<Guess> findByExternalKey(UUID externalKey);

  /**
   * Retrieves a {@link Guess} by its internal database ID.
   *
   * @param id ID of the guess.
   * @return Optional containing the {@link Guess}, if found.
   */
  Optional<Guess> findById(long id);

  /**
   * Retrieves a {@link Guess} associated with the given {@link UserPuzzle} and external UUID key.
   *
   * @param userPuzzle {@link UserPuzzle} to which the guess belongs.
   * @param externalKey UUID of the guess.
   * @return Optional containing the {@link Guess}, if found.
   */
  Optional<Guess> findByUserPuzzleAndExternalKey(UserPuzzle userPuzzle, UUID externalKey);

  /**
   * Retrieves all {@link Guess} entities associated with the given {@link UserPuzzle}.
   *
   * @param userPuzzle {@link UserPuzzle} to which the guesses belong.
   * @return List of all associated guesses.
   */
  List<Guess> findAllByUserPuzzle(UserPuzzle userPuzzle);

  /**
   * Retrieves all {@link Guess} entities associated with the given {@link UserPuzzle},
   * ordered by ID in descending order.
   *
   * @param userPuzzle {@link UserPuzzle} to which the guesses belong.
   * @return Iterable of guesses ordered by descending ID.
   */
  Iterable<Guess> findByUserPuzzleOrderByIdDesc(UserPuzzle userPuzzle);

  /**
   * Retrieves all {@link Guess} entities associated with the given {@link UserPuzzle},
   * ordered by ID in ascending order.
   *
   * @param userPuzzle {@link UserPuzzle} to which the guesses belong.
   * @return Iterable of guesses ordered by ascending ID.
   */
  Iterable<Guess> findByUserPuzzleOrderByIdAsc(UserPuzzle userPuzzle);

  /**
   * Retrieves all {@link Guess} entities associated with the given {@link UserPuzzle},
   * ordered by creation timestamp in ascending order.
   *
   * @param puzzle {@link UserPuzzle} to which the guesses belong.
   * @return List of guesses ordered by creation time.
   */
  List<Guess> findByUserPuzzleOrderByCreatedAsc(UserPuzzle puzzle);

  /**
   * Retrieves all {@link Guess} entities associated with the given {@link UserPuzzle},
   * ordered by creation timestamp in descending order.
   *
   * @param userPuzzle {@link UserPuzzle} to which the guesses belong.
   * @return Iterable of guesses ordered by most recently created first.
   */
  Iterable<Guess> findByUserPuzzleOrderByCreatedDesc(UserPuzzle userPuzzle);

}
