package edu.cnm.deepdive.crossfyre.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/{key}/{userpuzzle}")
@Validated
@Profile("service")
public class PuzzleWordController {



}
