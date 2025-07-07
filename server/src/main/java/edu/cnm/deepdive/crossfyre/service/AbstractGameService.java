package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.UserWord;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import java.util.UUID;

public interface AbstractGameService {

  Iterable<UserWord> getAllInPuzzle(UUID puzzleKey);

  UserWord add(User author, UUID puzzleKey, UserWord userWord);

  UserWord get(UUID puzzleKey, UUID wordKey);

}
