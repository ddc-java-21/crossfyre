package edu.cnm.deepdive.crossfyre.controller;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.Puzzle.Board;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import edu.cnm.deepdive.crossfyre.service.AbstractGeneratorService;
import edu.cnm.deepdive.crossfyre.service.AbstractPuzzleService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides endpoints for retrieving and generating crossword puzzles.
 * This controller is active only in the {@code service} profile.
 *
 * <p>Exposes puzzle retrieval by date and puzzle word generation
 * using a predefined board configuration.</p>
 */
@RestController
@RequestMapping("/puzzles")
@Validated
@Profile("service")
public class PuzzleController {

  private final AbstractPuzzleService service;
  private final AbstractGeneratorService generator;

  /**
   * Initializes this controller with the necessary puzzle and generator services.
   *
   * @param service   Service for accessing and managing puzzle data.
   * @param generator Service for generating new puzzles.
   */
  @Autowired
  public PuzzleController(AbstractPuzzleService service, AbstractGeneratorService generator) {
    this.service = service;
    this.generator = generator;
  }

  /**
   * Retrieves the {@link Puzzle} entity corresponding to the given date.
   *
   * @param date The {@link LocalDate} for which to retrieve the puzzle.
   * @return A {@link Puzzle} instance matching the provided date.
   */
  @GetMapping(path = "/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Puzzle get(@PathVariable LocalDate date) {
    return service.get(date);
  }

  /**
   * Generates a new list of {@link PuzzleWord} instances using the {@code SATURDAY} board layout.
   * This endpoint is typically used for testing, development, or preview purposes.
   *
   * @return A list of generated {@link PuzzleWord} objects.
   */
  @GetMapping(path = "/generate", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<PuzzleWord> generate() {
    return generator.generatePuzzleWords(Board.SATURDAY);
  }

}
