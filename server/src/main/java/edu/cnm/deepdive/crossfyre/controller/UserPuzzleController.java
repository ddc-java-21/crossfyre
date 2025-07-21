package edu.cnm.deepdive.crossfyre.controller;

import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import edu.cnm.deepdive.crossfyre.service.AbstractPuzzleService;
import edu.cnm.deepdive.crossfyre.service.AbstractUserPuzzleService;
import edu.cnm.deepdive.crossfyre.service.AbstractUserService;
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
@RequestMapping("/userpuzzles")
@Validated
@Profile("service")
public class UserPuzzleController {

  private final AbstractUserPuzzleService userPuzzleService;
  private final AbstractUserService userService;
  private final AbstractPuzzleService puzzleService;

  @Autowired
  UserPuzzleController(AbstractUserPuzzleService userPuzzleService, AbstractUserService userService,
      AbstractPuzzleService puzzleService) {
    this.userPuzzleService = userPuzzleService;
    this.userService = userService;
    this.puzzleService = puzzleService;
  }

  @GetMapping(path = "/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
  public UserPuzzle get(@PathVariable Instant date) {
    return userPuzzleService.getOrAddUserPuzzle(userService.getCurrentUser(), puzzleService.get(date));
  }

//  @GetMapping(path = "/{userPuzzleKey}", produces = MediaType.APPLICATION_JSON_VALUE)
//  public UserPuzzle get(@PathVariable UUID userPuzzleKey) {
//    return userPuzzleService.get(userPuzzleKey);
//  }

}
