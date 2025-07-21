package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre
    .model.entity.User;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByOauthKey(String oauthKey);

  Optional<User> findByExternalKey(UUID key);

  @Query("SELECT up FROM UserPuzzle as up WHERE up.user = :user AND up.puzzle = (SELECT p from Puzzle as p WHERE p.date = :date)")
  Optional<UserPuzzle> findByUserAndPuzzleDate(User user, Instant date);

}
