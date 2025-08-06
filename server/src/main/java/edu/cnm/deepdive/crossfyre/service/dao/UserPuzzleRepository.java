package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for managing {@link UserPuzzle} entities. Provides CRUD operations and
 * custom queries for accessing a user's interaction with specific puzzles based on user identity,
 * puzzle identity, date, and creation timestamps.
 */
public interface UserPuzzleRepository extends CrudRepository<UserPuzzle, Long> {

  /**
   * Retrieves a {@link UserPuzzle} by its external UUID key.
   *
   * @param externalKey External unique identifier of the user-puzzle record.
   * @return An {@link Optional} containing the {@link UserPuzzle} if found.
   */
  Optional<UserPuzzle> findByExternalKey(UUID externalKey);

  /**
   * Retrieves a {@link UserPuzzle} by a specific user and puzzle's external UUID key.
   *
   * @param user              The user who solved or interacted with the puzzle.
   * @param puzzleExternalKey External UUID key of the puzzle.
   * @return An {@link Optional} containing the {@link UserPuzzle} if found.
   */
  Optional<UserPuzzle> findByUserAndPuzzleExternalKey(User user, UUID puzzleExternalKey);

  /**
   * Retrieves a {@link UserPuzzle} by a specific user and puzzle.
   *
   * @param user   The user associated with the puzzle.
   * @param puzzle The puzzle entity.
   * @return An {@link Optional} containing the {@link UserPuzzle} if found.
   */
  Optional<UserPuzzle> findByUserAndPuzzle(User user, Puzzle puzzle);

  /**
   * Retrieves a {@link UserPuzzle} by a user and a puzzle date.
   * Uses a nested query to match the puzzle by date.
   *
   * @param user The user associated with the puzzle.
   * @param date The date the puzzle was published.
   * @return An {@link Optional} containing the {@link UserPuzzle} if found.
   */
  @Query("SELECT up FROM UserPuzzle as up WHERE up.user = :user AND up.puzzle = (SELECT p from Puzzle as p WHERE p.date = :date)")
  Optional<UserPuzzle> findByUserAndPuzzleDate(User user, LocalDate date);

  /**
   * Retrieves all {@link UserPuzzle} records associated with a user, ordered by creation time in descending order.
   *
   * @param user The user whose records are to be retrieved.
   * @return An {@link Iterable} collection of {@link UserPuzzle} instances.
   */
  Iterable<UserPuzzle> findByUserOrderByCreatedDesc(User user);

  /**
   * Retrieves all {@link UserPuzzle} records for a given puzzle, ordered by creation time in descending order.
   *
   * @param puzzle The puzzle for which user interactions are to be retrieved.
   * @return An {@link Iterable} collection of {@link UserPuzzle} instances.
   */
  Iterable<UserPuzzle> findByPuzzleOrderByCreatedDesc(Puzzle puzzle);

  /**
   * Retrieves a {@link UserPuzzle} by its internal database ID.
   *
   * @param id The internal database ID of the user-puzzle record.
   * @return An {@link Optional} containing the {@link UserPuzzle} if found.
   */
  Optional<UserPuzzle> findById(long id);

}
