package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Guess;
import java.time.Instant;
import java.util.UUID;

public interface AbstractGuessService {

  public Iterable<Guess> getAllInUserPuzzle(UUID userPuzzleKey);

  public Iterable<Guess> getAllInUserPuzzle(Instant puzzleDate);

}
