package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import java.time.Instant;

public interface AbstractUserPuzzleService {

  Iterable<UserPuzzle> getAll(User user, Puzzle puzzle);

  UserPuzzle getOrAddUserPuzzle(User user, Puzzle puzzle);

  UserPuzzle get(User user, Instant date);

}
