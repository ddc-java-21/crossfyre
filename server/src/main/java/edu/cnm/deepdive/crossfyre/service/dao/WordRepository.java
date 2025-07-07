package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import edu.cnm.deepdive.crossfyre.model.entity.UserWord;
import edu.cnm.deepdive.crossfyre.model.projection.Posted;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface WordRepository extends CrudRepository<UserWord, Long> {

  Optional<UserWord> findByExternalKey(UUID externalKey);

  Optional<UserWord> findByPuzzleAndExternalKey(UserPuzzle userPuzzle, UUID externalKey);

  Iterable<UserWord> findByPuzzleOrderByPostedAsc(UserPuzzle userPuzzle);

//  This is included to show a JPQL implementation of a Spring Data inferred query.
//  @Query("SELECT m FROM Message AS m WHERE m.Puzzle = :Puzzle AND m.posted > :cutoff")
  Iterable<UserWord> findByPuzzleAndPostedAfterOrderByPostedAsc(UserPuzzle userPuzzle, Instant cutoff);

  Optional<Posted> findFirstByPuzzleAndPostedAfterOrderByPostedDesc(UserPuzzle userPuzzle, Instant cutoff);

}
