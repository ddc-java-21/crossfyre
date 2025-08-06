package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for managing {@link PuzzleWord} entities. This interface provides
 * CRUD operations and custom queries to retrieve words based on their puzzle, position,
 * external key, and other criteria.
 */
public interface PuzzleWordRepository extends CrudRepository<PuzzleWord, Long> {

  /**
   * Retrieves a {@link PuzzleWord} by its external UUID key.
   *
   * @param externalKey External unique identifier of the word.
   * @return An {@link Optional} containing the word if found.
   */
  Optional<PuzzleWord> findByExternalKey(UUID externalKey);

  /**
   * Retrieves a {@link PuzzleWord} by its internal database ID.
   *
   * @param id Internal database ID of the word.
   * @return An {@link Optional} containing the word if found.
   */
  Optional<PuzzleWord> findById(long id);

  /**
   * Retrieves a {@link PuzzleWord} by its puzzle's date and the word's external key.
   *
   * @param date        Date of the puzzle.
   * @param externalKey External UUID key of the word.
   * @return An {@link Optional} containing the word if found.
   */
  Optional<PuzzleWord> findByPuzzleDateAndExternalKey(LocalDate date, UUID externalKey);

  /**
   * Retrieves a {@link PuzzleWord} by its word name (solution).
   *
   * @param wordName The solution of the word.
   * @return An {@link Optional} containing the word if found.
   */
  Optional<PuzzleWord> findBywordName(String wordName);

  /**
   * Retrieves a {@link PuzzleWord} by the puzzle it belongs to and its external UUID key.
   *
   * @param puzzle      Puzzle to which the word belongs.
   * @param externalKey External UUID key of the word.
   * @return An {@link Optional} containing the word if found.
   */
  Optional<PuzzleWord> findByPuzzleAndExternalKey(Puzzle puzzle, UUID externalKey);

  /**
   * Retrieves a {@link PuzzleWord} by its position in the puzzle grid.
   *
   * @param wordRow    Row index of the word.
   * @param wordColumn Column index of the word.
   * @return An {@link Optional} containing the word if found.
   */
  Optional<PuzzleWord> findByWordPosition_WordRowAndWordPosition_WordColumn(int wordRow, int wordColumn);

  /**
   * Retrieves all {@link PuzzleWord} instances for a puzzle published on a specific date.
   *
   * @param date Date of the puzzle.
   * @return Iterable collection of puzzle words.
   */
  Iterable<PuzzleWord> findByPuzzleDate(LocalDate date);

  /**
   * Retrieves all {@link PuzzleWord} instances for a given puzzle.
   *
   * @param puzzle Puzzle to which the words belong.
   * @return Iterable collection of puzzle words.
   */
  Iterable<PuzzleWord> findByPuzzle(Puzzle puzzle);

  /**
   * Retrieves a {@link PuzzleWord} by its clue text.
   *
   * @param clue The clue associated with the word.
   * @return An {@link Optional} containing the word if found.
   */
  Optional<PuzzleWord> findByClue(String clue);
}
