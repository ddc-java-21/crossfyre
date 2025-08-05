package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Guess;
import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import java.time.Instant;
import java.util.UUID;

public interface AbstractUserPuzzleService {

  Iterable<UserPuzzle> getAllByPuzzle(UUID puzzleKey);

  Iterable<UserPuzzle> getAllByPuzzleDate(Instant date);

  Iterable<UserPuzzle> getAllByUser(User user);


  UserPuzzle getOrAddUserPuzzle(User user, Puzzle puzzle);

  UserPuzzle add(User requestor, Instant puzzleDate, Guess guess);

  UserPuzzle get(User user, Instant date);

  UserPuzzle get(UUID externalKey);


}
