package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.SolutionPuzzle;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import edu.cnm.deepdive.crossfyre.model.entity.UserWord;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import java.time.Instant;
import java.util.UUID;
import org.springframework.web.context.request.async.DeferredResult;

public interface AbstractGameService {

  UserWord get(UserPuzzle puzzleKey, UUID wordKey);

  Iterable<UserWord> getAllInPuzzle(UUID puzzleKey);

  DeferredResult<Iterable<UserWord>> getAllInPuzzleSince(UUID puzzleKey, Instant cutoff);

  UserWord add(User author, UUID puzzleKey, UserWord userWord);

  UserWord get(UUID puzzleKey, UUID wordKey);

  UserWord add(User author, UUID puzzleKey, UserWord userWord, SolutionPuzzle solutionPuzzle,
      UserPuzzle userPuzzle);
}
