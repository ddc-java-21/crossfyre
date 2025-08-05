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
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
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
 * REST controller for managing user guesses for a specific puzzle on a given date.
 * This controller provides endpoints for retrieving all guesses, retrieving a specific guess,
 * and submitting a new guess for the current authenticated user.
 *
 * <p>All routes are scoped under <code>/userpuzzles/{date}/guesses</code>.</p>
 */
@RestController
@RequestMapping("/userpuzzles/{date}/guesses")
@Validated
@Profile("service")
public class GuessController {

  private final AbstractGuessService guessService;
  private final AbstractUserService userService;
  private final AbstractUserPuzzleService userPuzzleService;

  /**
   * Constructs the controller with injected services for handling guesses,
   * user accounts, and user-puzzle state management.
   *
   * @param guessService        Service responsible for managing guesses.
   * @param userService         Service for accessing the current authenticated user.
   * @param userPuzzleService   Service for tracking the user's interaction with puzzles.
   */
  @Autowired
  public GuessController(
      AbstractGuessService guessService,
      AbstractUserService userService,
      UserPuzzleService userPuzzleService
  ) {
    this.guessService = guessService;
    this.userService = userService;
    this.userPuzzleService = userPuzzleService;
  }

  /**
   * Retrieves all guesses made by the current user for the puzzle on the specified date.
   *
   * @param date Date of the puzzle.
   * @return Iterable list of {@link Guess} entities associated with the user and puzzle.
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Guess> get(@PathVariable LocalDate date) {
    return guessService.getAllInUserPuzzle(userService.getCurrentUser(), date);
  }

  /**
   * Retrieves a specific guess by its unique key for the current user and specified puzzle date.
   *
   * @param date     Date of the puzzle.
   * @param guessKey UUID of the guess to retrieve.
   * @return The {@link Guess} entity matching the given key.
   */
  @GetMapping(path = "/{guessKey}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Guess get(@PathVariable LocalDate date, @PathVariable UUID guessKey) {
    return guessService.get(userService.getCurrentUser(), date, guessKey);
  }

  /**
   * Submits a new guess for the current user and the puzzle on the specified date.
   *
   * @param date              Date of the puzzle.
   * @param guessEndpointDto  DTO containing the guessed character and grid position.
   * @return Updated {@link UserPuzzle} after the guess has been processed and applied.
   */
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
