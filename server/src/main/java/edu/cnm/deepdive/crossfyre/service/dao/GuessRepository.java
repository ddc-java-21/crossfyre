package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.Guess;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface GuessRepository extends CrudRepository<Guess, Character> {

  Optional<Guess> findByExternalKey(UUID externalKey);

  Optional<Guess> findByUserPuzzleAndExternalKey(UserPuzzle puzzle, UUID externalKey);

  Iterable<Guess> findByUserPuzzleOrderByCreatedAsc(UserPuzzle puzzle);

  //  This is included to show a JPQL implementation of a Spring Data inferred query.
//  @Query("SELECT m FROM Message AS m WHERE m.UserPuzzle = :UserPuzzle AND m.posted > :cutoff")
  Iterable<Guess> findByUserPuzzleAndCreatedAfterOrderByCreatedAsc(UserPuzzle puzzle, Instant cutoff);

  Optional<Guess> findFirstByUserPuzzleAndCreatedAfterOrderByCreatedDesc(UserPuzzle UserPuzzle, Instant cutoff);

}
