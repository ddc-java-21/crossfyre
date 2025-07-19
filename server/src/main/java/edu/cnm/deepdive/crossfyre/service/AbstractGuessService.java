package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Guess;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import java.time.Instant;
import java.util.UUID;

public interface AbstractGuessService {

  Iterable<Guess> getAllInUserPuzzle(UUID userPuzzleKey, User user, Instant date);

  Guess add(UUID userPuzzleKey, User user, Instant date, Guess guess);

  Guess get(UUID userPuzzleKey, User user, Instant date, UUID guessKey);

//  Iterable<Guess> getAllInUserPuzzle(Instant puzzleDate);

}
