package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.Guess;
import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface GuessRepository extends CrudRepository<Guess, Long> {

  Optional<Guess> findByExternalKey(UUID externalKey);

  Optional<Guess> findByUserPuzzleAndExternalKey(UserPuzzle userPuzzle, UUID externalKey);

  Iterable<Guess> findByUserPuzzleOrderByIdDesc(UserPuzzle userPuzzle);

  Iterable<Guess> findByUserPuzzleOrderByCreatedDesc(UserPuzzle userPuzzle);

}
