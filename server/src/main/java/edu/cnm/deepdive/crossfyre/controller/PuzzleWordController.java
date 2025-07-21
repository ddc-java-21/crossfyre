package edu.cnm.deepdive.crossfyre.controller;

import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord.Direction;
import edu.cnm.deepdive.crossfyre.service.AbstractPuzzleService;
import edu.cnm.deepdive.crossfyre.service.AbstractPuzzleWordService;
import java.time.Instant;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/puzzle/{puzzleDate}/puzzleWords")
@Validated
@Profile("service")
public class PuzzleWordController {

  private final AbstractPuzzleWordService puzzleWordService;

  @Autowired
  public PuzzleWordController(AbstractPuzzleWordService puzzleWordService) {
    this.puzzleWordService = puzzleWordService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<PuzzleWord> get(@PathVariable Instant puzzleDate) {
    return puzzleWordService.getAllInPuzzle(puzzleDate);
  }

//  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//  public PuzzleWord get(@PathVariable Instant puzzleDate, int wordRow, int wordColumn, int wordLength, Direction direction) {
//    return puzzleWordService.get(puzzleDate, wordRow, wordColumn, wordLength, direction);
//  }

}
