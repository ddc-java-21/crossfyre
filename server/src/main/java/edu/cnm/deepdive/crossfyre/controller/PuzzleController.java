package edu.cnm.deepdive.crossfyre.controller;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.service.AbstractPuzzleService;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/puzzle/{puzzleDate}")
public class PuzzleController {

  private final AbstractPuzzleService puzzleService;

  @Autowired
  public PuzzleController(AbstractPuzzleService puzzleService) {
    this.puzzleService = puzzleService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Puzzle get(@PathVariable Instant puzzleDate) {
    return puzzleService.get(puzzleDate);
  }


}
