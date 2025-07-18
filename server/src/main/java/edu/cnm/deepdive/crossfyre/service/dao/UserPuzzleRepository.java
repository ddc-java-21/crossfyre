package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import edu.cnm.deepdive.crossfyre.model.projection.Created;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface UserPuzzleRepository extends CrudRepository<UserPuzzle, Long> {

  Optional<UserPuzzle> findById(long id);

  Optional<UserPuzzle> findByUserAndUserPuzzle(User user, UserPuzzle puzzle);

  Optional<UserPuzzle> findByUserAndDate(User user, Instant date);

  // Optional<List<UserPuzzle>> findByUserAndDateSince(User user, Instant date);

}
