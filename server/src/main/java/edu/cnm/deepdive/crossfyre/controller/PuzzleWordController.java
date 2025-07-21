package edu.cnm.deepdive.crossfyre.controller;

import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import edu.cnm.deepdive.crossfyre.service.AbstractPuzzleService;
import edu.cnm.deepdive.crossfyre.service.AbstractPuzzleWordService;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.Instant;
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

@RestController
@RequestMapping("/puzzles/{date}/puzzleWords")
@Validated
@Profile("service")
public class PuzzleWordController {

  private final AbstractPuzzleWordService puzzleWordService;
  private final AbstractPuzzleService puzzleService;

  @Autowired
  PuzzleWordController(AbstractPuzzleWordService puzzleWordService, AbstractPuzzleService puzzleService) {
    this.puzzleWordService = puzzleWordService;
    this.puzzleService = puzzleService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<PuzzleWord> get(@PathVariable Instant date) {
    return puzzleWordService.getAllInPuzzle(date);
  }

  @GetMapping(path = "/{puzzleWordKey}", produces = MediaType.APPLICATION_JSON_VALUE)
  public PuzzleWord get(@PathVariable Instant date, @PathVariable UUID puzzleWordKey) {
    return puzzleWordService.get(date, puzzleWordKey);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PuzzleWord> post(@PathVariable Instant date, @Valid @RequestBody PuzzleWord puzzleWord) {
    PuzzleWord created =  puzzleWordService.add(date, puzzleWord);
    URI location = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(getClass())
                .get(date, created.getExternalKey())
        )
        .toUri();
    return ResponseEntity.created(location).body(created);
  }


}
