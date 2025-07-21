package edu.cnm.deepdive.crossfyre.controller;

import edu.cnm.deepdive.crossfyre.model.entity.Guess;
import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import edu.cnm.deepdive.crossfyre.service.AbstractPuzzleService;
import edu.cnm.deepdive.crossfyre.service.AbstractUserPuzzleService;
import edu.cnm.deepdive.crossfyre.service.AbstractUserService;
import edu.cnm.deepdive.crossfyre.service.AbstractGuessService;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("/userpuzzles/{date}/guesses")
@Validated
@Profile("service")
public class GuessController {

  private final AbstractGuessService guessService;
  private final AbstractUserPuzzleService userPuzzleService;
  private final AbstractUserService userService;
  private final AbstractPuzzleService puzzleService;
  private Puzzle puzzle;
  private User currentUser;

  @Autowired
  GuessController(AbstractGuessService guessService, AbstractUserPuzzleService userPuzzleService, AbstractUserService userService,
      AbstractPuzzleService puzzleService) {
    this.guessService = guessService;
    this.userPuzzleService = userPuzzleService;
    this.userService = userService;
    this.puzzleService = puzzleService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Guess> get(@PathVariable Instant date) {
    return guessService.getAllInUserPuzzle(userService.getCurrentUser(), date);
  }

  @GetMapping(path = "/{guessKey}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Guess get(@PathVariable Instant date, @PathVariable UUID guessKey) {
    return guessService.get(userService.getCurrentUser(), date, guessKey);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Guess> post(@PathVariable Instant date, @Valid @RequestBody Guess guess) {
    Guess created =  guessService.add(userService.getCurrentUser(), date, guess);
    // Do we need to update UserPuzzle from here? messageService.add() in chat implies no, but...
    UserPuzzle currentUserPuzzle = userPuzzleService.get(currentUser, date); // should get last saved version of game
    List<Guess> guesses = new ArrayList<>();
    Iterable<Guess> guessesIt = guessService.getAllInUserPuzzle(currentUser, date);
    for (Guess g : guessesIt) {
      guesses.add(g);
    }
    UserPuzzle delta = new UserPuzzle();
    delta.setUser(currentUser);
    delta.setPuzzle(puzzle);
    delta.setGuesses(guesses);
    userPuzzleService.updateUserPuzzle(currentUserPuzzle, delta);
    URI location = WebMvcLinkBuilder.linkTo(
      WebMvcLinkBuilder.methodOn(getClass())
          .get(date, created.getExternalKey())
    )
        .toUri();
    return ResponseEntity.created(location).body(created);
  }

}
