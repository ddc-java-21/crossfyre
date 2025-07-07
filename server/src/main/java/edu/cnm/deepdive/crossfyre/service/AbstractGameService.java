package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Guess;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import java.util.UUID;

public interface AbstractGameService {

  Iterable<Guess> getAllInPuzzle(UUID puzzleKey);

  Guess add(User author, UUID puzzleKey, Guess guess);

  Guess get(UUID puzzleKey, UUID wordKey);

}
