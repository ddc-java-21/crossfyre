package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import java.util.UUID;

public interface AbstractPuzzleService {

  Iterable<UserPuzzle> getAll();

  UserPuzzle get(UUID key);

}
