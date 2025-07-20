package edu.cnm.deepdive.crossfyre.controller;

import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import edu.cnm.deepdive.crossfyre.service.AbstractUserPuzzleService;
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

  @Autowired
  UserPuzzleController(AbstractUserPuzzleService userPuzzleService) {
    this.userPuzzleService = userPuzzleService;
  }



  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<UserPuzzle> get() {
    return service.getAll();
  }

  @GetMapping(path = "/{userPuzzleKey}", produces = MediaType.APPLICATION_JSON_VALUE)
  public UserPuzzle get(@PathVariable UUID userPuzzleKey) {
    return userPuzzleService.get(userPuzzleKey);
  }

}
