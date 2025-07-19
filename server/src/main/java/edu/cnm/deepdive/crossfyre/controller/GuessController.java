package edu.cnm.deepdive.crossfyre.controller;


import edu.cnm.deepdive.crossfyre.model.entity.Guess;
import edu.cnm.deepdive.crossfyre.service.AbstractGuessService;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crossfyre/userpuzzle/")
@Validated
@Profile("service")
public class GuessController {

  // TODO: 7/18/2025 Add Dorian's endpoints

  private final AbstractGuessService guessService;


  public GuessController(AbstractGuessService guessService) {
    this.guessService = guessService;
  }



}
