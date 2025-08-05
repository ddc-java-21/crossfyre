package edu.cnm.deepdive.crossfyre.controller;

import edu.cnm.deepdive.crossfyre.model.dto.endpoint.GuessEndpointDto;
import edu.cnm.deepdive.crossfyre.model.entity.Guess;
import edu.cnm.deepdive.crossfyre.model.entity.Guess.GuessPosition;
import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import edu.cnm.deepdive.crossfyre.service.AbstractPuzzleService;
import edu.cnm.deepdive.crossfyre.service.AbstractUserPuzzleService;
import edu.cnm.deepdive.crossfyre.service.AbstractUserService;
import edu.cnm.deepdive.crossfyre.service.AbstractGuessService;
import edu.cnm.deepdive.crossfyre.service.UserPuzzleService;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
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
  private final AbstractUserService userService;
  private final AbstractUserPuzzleService userPuzzleService;

  private Puzzle puzzle;


  @Autowired
  GuessController(AbstractGuessService guessService, AbstractUserService userService,
      UserPuzzleService userPuzzleService) {
    this.guessService = guessService;
    this.userService = userService;
    this.userPuzzleService = userPuzzleService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Guess> get(@PathVariable LocalDate date) {
    return guessService.getAllInUserPuzzle(userService.getCurrentUser(), date);
  }

  @GetMapping(path = "/{guessKey}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Guess get(@PathVariable LocalDate date, @PathVariable UUID guessKey) {
    return guessService.get(userService.getCurrentUser(), date, guessKey);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public UserPuzzle post(@PathVariable LocalDate date, @Valid @RequestBody GuessEndpointDto guessEndpointDto) {
    Guess guess = new Guess();
    guess.setGuessChar(guessEndpointDto.getGuess().charAt(0));
    guess.setGuessPosition(new GuessPosition(
        guessEndpointDto.getGuessPosition().getRow(),
        guessEndpointDto.getGuessPosition().getColumn()
    ));
    return userPuzzleService.add(userService.getCurrentUser(), date, guess);
  }

}
