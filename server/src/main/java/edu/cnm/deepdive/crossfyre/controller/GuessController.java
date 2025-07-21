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
@RequestMapping("/userPuzzles/{userPuzzleDate}/guesses")
@Validated
@Profile("service")
public class GuessController {

  // TODO: 7/18/2025 Add Dorian's endpoints

  private final AbstractGuessService guessService;
  private final UserService userService;

  @Autowired
  public GuessController(AbstractGuessService guessService, UserService userService) {
    this.guessService = guessService;
    this.userService = userService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Guess> get(User user, @PathVariable Instant userPuzzleDate) {
    return guessService.getAllInUserPuzzle(user, userPuzzleDate);
  }

//  @GetMapping(path = "/{guessKey}", produces = MediaType.APPLICATION_JSON_VALUE)
//  public Guess get(User user, Instant date, @PathVariable UUID guessKey) {
//    return guessService.get(user, date, guessKey);
//  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Guess> post(@Valid @RequestBody Guess guess, @PathVariable Instant userPuzzleDate) {
    Guess created = guessService.add(userService.getCurrentUser(), userPuzzleDate, guess);
    URI location = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(getClass())
                .get(userService.getCurrentUser(), userPuzzleDate)
        )
        .toUri();
    return ResponseEntity.created(location).body(created);
  }

}
