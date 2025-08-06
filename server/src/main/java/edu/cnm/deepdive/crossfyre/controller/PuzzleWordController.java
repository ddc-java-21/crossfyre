package edu.cnm.deepdive.crossfyre.controller;

import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import edu.cnm.deepdive.crossfyre.service.AbstractPuzzleService;
import edu.cnm.deepdive.crossfyre.service.AbstractPuzzleWordService;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides endpoints for accessing and managing {@link PuzzleWord} entities
 * associated with a {@link edu.cnm.deepdive.crossfyre.model.entity.Puzzle} on a given date.
 *
 * <p>This controller is active only when the {@code service} Spring profile is active.</p>
 *
 * <p>Routes are rooted at <code>/puzzles/{date}/puzzleWords</code>.</p>
 */
@RestController
@RequestMapping("/puzzles/{date}/puzzleWords")
@Validated
@Profile("service")
public class PuzzleWordController {

  private final AbstractPuzzleWordService puzzleWordService;
  private final AbstractPuzzleService puzzleService;

  /**
   * Initializes this controller with the necessary puzzle and puzzle word services.
   *
   * @param puzzleWordService Service for managing {@link PuzzleWord} entities.
   * @param puzzleService     Service for managing {@link edu.cnm.deepdive.crossfyre.model.entity.Puzzle} entities.
   */
  @Autowired
  public PuzzleWordController(
      AbstractPuzzleWordService puzzleWordService,
      AbstractPuzzleService puzzleService
  ) {
    this.puzzleWordService = puzzleWordService;
    this.puzzleService = puzzleService;
  }

  /**
   * Retrieves all {@link PuzzleWord} instances for the puzzle on the specified date.
   *
   * @param date The {@link LocalDate} representing the puzzle's publication date.
   * @return An iterable collection of {@link PuzzleWord} entities.
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<PuzzleWord> get(@PathVariable LocalDate date) {
    return puzzleWordService.getAllInPuzzle(date);
  }

  /**
   * Retrieves a specific {@link PuzzleWord} by its external key, scoped to a puzzle on a given date.
   *
   * @param date           The {@link LocalDate} representing the puzzle's publication date.
   * @param puzzleWordKey  The {@link UUID} identifying the {@link PuzzleWord}.
   * @return The requested {@link PuzzleWord} instance.
   */
  @GetMapping(path = "/{puzzleWordKey}", produces = MediaType.APPLICATION_JSON_VALUE)
  public PuzzleWord get(@PathVariable LocalDate date, @PathVariable UUID puzzleWordKey) {
    return puzzleWordService.get(date, puzzleWordKey);
  }

  /**
   * Creates a new {@link PuzzleWord} and associates it with the puzzle on the specified date.
   *
   * <p>The created resource is returned in the response body, and its URI is included
   * in the {@code Location} header.</p>
   *
   * @param date        The {@link LocalDate} representing the puzzle's publication date.
   * @param puzzleWord  The {@link PuzzleWord} to be added (validated).
   * @return A {@link ResponseEntity} containing the created {@link PuzzleWord} and location URI.
   */
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PuzzleWord> post(
      @PathVariable LocalDate date,
      @Valid @RequestBody PuzzleWord puzzleWord
  ) {
    PuzzleWord created = puzzleWordService.add(date, puzzleWord);
    URI location = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(getClass()).get(date, created.getExternalKey())
        )
        .toUri();
    return ResponseEntity.created(location).body(created);
  }

}
