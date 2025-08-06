package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for {@link Puzzle} entities. Provides CRUD operations and
 * custom queries to retrieve puzzles by ID, external key, or puzzle date.
 */
public interface PuzzleRepository extends CrudRepository<Puzzle, Long> {

  /**
   * Retrieves a {@link Puzzle} by its internal database ID.
   *
   * @param id Internal ID of the puzzle.
   * @return Optional containing the {@link Puzzle} if found; otherwise, an empty {@link Optional}.
   */
  Optional<Puzzle> findById(long id);

  /**
   * Retrieves a {@link Puzzle} by its external UUID key.
   *
   * @param externalKey UUID key assigned to the puzzle.
   * @return Optional containing the {@link Puzzle} if found; otherwise, an empty {@link Optional}.
   */
  Optional<Puzzle> findByExternalKey(UUID externalKey);

  /**
   * Retrieves a {@link Puzzle} by its associated calendar date.
   *
   * @param date The date the puzzle is associated with (typically a publishing or availability date).
   * @return Optional containing the {@link Puzzle} if found; otherwise, an empty {@link Optional}.
   */
  Optional<Puzzle> findByDate(LocalDate date);

}
