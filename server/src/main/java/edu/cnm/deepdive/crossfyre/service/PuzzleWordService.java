package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleRepository;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleWordRepository;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Service class for managing {@link PuzzleWord} entities.
 * <p>
 * Provides methods to retrieve, add, and get puzzle words associated with puzzles by date.
 * </p>
 */
@Service
@Profile("service")
public class PuzzleWordService implements AbstractPuzzleWordService {

  private final PuzzleWordRepository puzzleWordRepository;
  private final PuzzleRepository puzzleRepository;

  /**
   * Constructs a {@code PuzzleWordService} with required repositories.
   *
   * @param puzzleWordRepository repository for puzzle words
   * @param puzzleRepository     repository for puzzles
   */
  @Autowired
  PuzzleWordService(PuzzleWordRepository puzzleWordRepository, PuzzleRepository puzzleRepository) {
    this.puzzleWordRepository = puzzleWordRepository;
    this.puzzleRepository = puzzleRepository;
  }

  /**
   * Retrieves all puzzle words associated with the puzzle on the specified date.
   *
   * @param date the date of the puzzle
   * @return an iterable of {@code PuzzleWord} objects for the puzzle on the given date
   * @throws java.util.NoSuchElementException if no puzzle exists for the given date
   */
  @Override
  public Iterable<PuzzleWord> getAllInPuzzle(LocalDate date) {
    return puzzleRepository
        .findByDate(date)
        .map(puzzleWordRepository::findByPuzzle)
        .orElseThrow();
  }

  /**
   * Adds a new puzzle word to the puzzle identified by the given date.
   *
   * @param date       the date of the puzzle to add the word to
   * @param puzzleWord the {@code PuzzleWord} to add
   * @return the saved {@code PuzzleWord}
   * @throws java.util.NoSuchElementException if no puzzle exists for the given date
   */
  @Override
  public PuzzleWord add(LocalDate date, PuzzleWord puzzleWord) {
    return puzzleRepository
        .findByDate(date)
        .map((puzzle) -> {
          puzzleWord.setPuzzle(puzzle);
          return puzzleWordRepository.save(puzzleWord);
        })
        .orElseThrow();
  }

  /**
   * Retrieves a specific puzzle word identified by its external UUID key
   * and the date of the puzzle it belongs to.
   *
   * @param date          the date of the puzzle
   * @param puzzleWordKey the UUID key of the puzzle word
   * @return the matching {@code PuzzleWord}
   * @throws java.util.NoSuchElementException if no matching puzzle word or puzzle exists
   */
  @Override
  public PuzzleWord get(LocalDate date, UUID puzzleWordKey) {
    return puzzleRepository
        .findByDate(date)
        .flatMap((puzzle) -> puzzleWordRepository.findByPuzzleDateAndExternalKey(date, puzzleWordKey))
        .orElseThrow();
  }

}
