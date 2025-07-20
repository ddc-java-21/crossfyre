package edu.cnm.deepdive.crossfyre.controller;


import com.fasterxml.jackson.annotation.JsonView;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import edu.cnm.deepdive.crossfyre.service.AbstractPuzzleService;
import edu.cnm.deepdive.crossfyre.service.AbstractUserPuzzleService;
import edu.cnm.deepdive.crossfyre.service.AbstractUserService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userpuzzles")
@Validated
@Profile("service")
public class UserPuzzleController {
  // TODO: 7/17/2025 Implement Start and Get game endpoints

  private final AbstractUserPuzzleService userPuzzleService;
  private final AbstractUserService userService;
  private final AbstractPuzzleService puzzleService;

  public UserPuzzleController(AbstractUserPuzzleService UserPuzzleService,
      AbstractUserService userService, AbstractPuzzleService puzzleService) {
    this.userPuzzleService = UserPuzzleService;
    this.userService = userService;
    this.puzzleService = puzzleService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @JsonView(User)
  public UserPuzzle get() {
    return userPuzzleService.get(userService.getCurrentUser(), puzzleService.getPuzzleWithDate(null).getDate());
  }

}
