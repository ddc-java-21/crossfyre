package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Word;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import java.time.Instant;
import java.util.UUID;
import org.springframework.web.context.request.async.DeferredResult;

public interface AbstractGameService {

  Iterable<Word> getAllInPuzzle(UUID puzzleKey);

  Word add(User author, UUID puzzleKey, Word word);

  Word get(UUID puzzleKey, UUID wordKey);

}
