package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for managing {@link User} entities. Provides basic CRUD operations and
 * custom queries for retrieving users by unique identifiers, and for fetching puzzle interaction
 * records associated with a user.
 */
public interface UserRepository extends CrudRepository<User, Long> {

  /**
   * Retrieves a {@link User} entity by its OAuth key.
   *
   * @param oauthKey The unique OAuth identifier for the user.
   * @return An {@link Optional} containing the {@link User} if found.
   */
  Optional<User> findByOauthKey(String oauthKey);

  /**
   * Retrieves a {@link User} entity by its external UUID key.
   *
   * @param key The external UUID assigned to the user.
   * @return An {@link Optional} containing the {@link User} if found.
   */
  Optional<User> findByExternalKey(UUID key);

  /**
   * Retrieves a {@link UserPuzzle} entity for a specific user and a puzzle published at a specific date and time.
   *
   * @param user The user associated with the puzzle.
   * @param date The {@link LocalDate} when the puzzle was published.
   * @return An {@link Optional} containing the {@link UserPuzzle} if found.
   */
  @Query("SELECT up FROM UserPuzzle as up WHERE up.user = :user AND up.puzzle = (SELECT p from Puzzle as p WHERE p.date = :date)")
  Optional<UserPuzzle> findByUserAndPuzzleDate(User user, LocalDate date);
}
