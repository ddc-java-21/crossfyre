package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import edu.cnm.deepdive.crossfyre.model.projection.Created;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface UserPuzzleRepository extends CrudRepository<UserPuzzle, Long> {

  Optional<UserPuzzle> findByExternalKey(UUID externalKey);

  Optional<UserPuzzle> findByPuzzleAndExternalKey(UserPuzzle userPuzzle, UUID externalKey);

  Iterable<UserPuzzle> findByPuzzleOrderByCreatedAsc(UserPuzzle userPuzzle);

//  This is included to show a JPQL implementation of a Spring Data inferred query.
//  @Query("SELECT m FROM Message AS m WHERE m.Puzzle = :Puzzle AND m.posted > :cutoff")
  Iterable<UserPuzzle> findByPuzzleAndCreatedAfterOrderByCreatedAsc(UserPuzzle userPuzzle, Instant cutoff);

  Optional<Created> findFirstByPuzzleAndCreatedAfterOrderByCreatedDesc(UserPuzzle userPuzzle, Instant cutoff);

}
