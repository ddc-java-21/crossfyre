package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Guess;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import java.time.Instant;
import java.util.UUID;

public interface AbstractGuessService {

  Iterable<Guess> getAllInUserPuzzle(User user, Instant date);

  Guess add(User user, Instant date, Guess guess);

  Guess get(User user, Instant date);

//  Iterable<Guess> getAllInUserPuzzle(Instant puzzleDate);

}
