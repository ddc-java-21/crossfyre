package edu.cnm.deepdive.crossfyre.controller;


import edu.cnm.deepdive.crossfyre.model.entity.Guess;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.service.AbstractGuessService;
import edu.cnm.deepdive.crossfyre.service.PuzzleService;
import edu.cnm.deepdive.crossfyre.service.UserPuzzleService;
import edu.cnm.deepdive.crossfyre.service.UserService;
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
@RequestMapping("/userPuzzles/{userPuzzleKey}/guesses")
@Validated
@Profile("service")
public class GuessController {

  // TODO: 7/18/2025 Add Dorian's endpoints

  private final AbstractGuessService guessService;
private final PuzzleService puzzleService;
private final UserService userService;

  @Autowired
  public GuessController(AbstractGuessService guessService, UserPuzzleService userPuzzleService,
      PuzzleService puzzleService,
      UserService userService) {
    this.guessService = guessService;
    this.puzzleService = puzzleService;
    this.userService = userService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Guess> get(@PathVariable UUID userPuzzleKey, User user, Instant date) {
    return guessService.getAllInUserPuzzle(userPuzzleKey, user, date);
  }

  @GetMapping(path = "/{guessKey}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Guess get(
      @PathVariable UUID userPuzzleKey, User user, Instant date,
      @PathVariable UUID guessKey) {
    return guessService.get(userPuzzleKey, user, date, guessKey);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Guess> post(@PathVariable UUID userPuzzleKey, @Valid @RequestBody Guess guess, Instant date) {
    Guess created = guessService.add(userPuzzleKey, userService.getCurrentUser(), date, guess);
    URI location = WebMvcLinkBuilder.linkTo(
        WebMvcLinkBuilder.methodOn(getClass())
            .get(created.getUserPuzzle().getExternalKey(), userService.getCurrentUser(), date)
    )
        .toUri();
    return ResponseEntity.created(location).body(created);
  }

}
