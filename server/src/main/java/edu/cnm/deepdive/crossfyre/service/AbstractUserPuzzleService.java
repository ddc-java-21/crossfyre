package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import java.time.Instant;
import java.util.UUID;

public interface AbstractUserPuzzleService {

  Iterable<UserPuzzle> getAllByPuzzle(UUID puzzleKey);

  Iterable<UserPuzzle> getAllByPuzzleDate(Instant date);

  Iterable<UserPuzzle> getAllByUser(User user);

//  UserPuzzle add(User user, Instant date, UserPuzzle userPuzzle);

  //UserPuzzle getOrAddUserPuzzle(User user, Instant date, UserPuzzle userPuzzle);

  UserPuzzle getOrAddUserPuzzle(User user, Instant date);

  UserPuzzle get(User user, Instant date);

  UserPuzzle get(UUID externalKey);

  //UserPuzzle get(Instant date, UUID userPuzzleKey);

  //UserPuzzle updateUserPuzzle(User user, Instant date, UserPuzzle userPuzzle);

  UserPuzzle updateUserPuzzle(UserPuzzle existing, UserPuzzle delta);

}
