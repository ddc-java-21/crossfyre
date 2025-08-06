package edu.cnm.deepdive.crossfyre.controller;

import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import edu.cnm.deepdive.crossfyre.service.AbstractPuzzleService;
import edu.cnm.deepdive.crossfyre.service.AbstractUserPuzzleService;
import edu.cnm.deepdive.crossfyre.service.AbstractUserService;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles HTTP requests related to {@link UserPuzzle} entities. Specifically, this controller
 * allows retrieval or creation of user-specific puzzle records by puzzle date.
 *
 * <p>This controller is only active under the {@code service} Spring profile.</p>
 */
@RestController
@RequestMapping("/userpuzzles")
@Validated
@Profile("service")
public class UserPuzzleController {

  private final AbstractUserPuzzleService userPuzzleService;
  private final AbstractUserService userService;
  private final AbstractPuzzleService puzzleService;

  /**
   * Initializes this controller with the required services for user puzzle, user, and puzzle operations.
   *
   * @param userPuzzleService service used to manage user-puzzle mappings.
   * @param userService       service used to access the currently authenticated user.
   * @param puzzleService     service used to retrieve puzzles by date.
   */
  @Autowired
  UserPuzzleController(AbstractUserPuzzleService userPuzzleService, AbstractUserService userService,
      AbstractPuzzleService puzzleService) {
    this.userPuzzleService = userPuzzleService;
    this.userService = userService;
    this.puzzleService = puzzleService;
  }
  /**
   * Retrieves an existing {@link UserPuzzle} for the authenticated user and the specified puzzle date,
   * or creates one if it doesn't exist.
   *
   * @param date the date of the puzzle to retrieve or associate with the user.
   * @return the corresponding {@link UserPuzzle} instance.
   */
  @GetMapping(path = "/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
  public UserPuzzle get(@PathVariable LocalDate date) {
    return userPuzzleService.getOrAddUserPuzzle(
        userService.getCurrentUser(),
        puzzleService.get(date)
    );
  }
}
