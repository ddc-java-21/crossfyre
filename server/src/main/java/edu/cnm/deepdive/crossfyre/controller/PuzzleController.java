package edu.cnm.deepdive.crossfyre.controller;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.Puzzle.Board;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import edu.cnm.deepdive.crossfyre.service.AbstractGeneratorService;
import edu.cnm.deepdive.crossfyre.service.AbstractPuzzleService;
import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/puzzles")
@Validated
@Profile("service")
public class PuzzleController {

  private final AbstractPuzzleService service;
  private final AbstractGeneratorService generator;

  @Autowired
  PuzzleController(AbstractPuzzleService service, AbstractGeneratorService generator) {
    this.service = service;
    this.generator = generator;
  }

  @GetMapping(path = "/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Puzzle get(@PathVariable Instant date) {
    return service.get(date);
  }

  @GetMapping(path = "/generate", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<PuzzleWord> generate() {
    return generator.generatePuzzleWords(Board.MONDAY);
  }

}
