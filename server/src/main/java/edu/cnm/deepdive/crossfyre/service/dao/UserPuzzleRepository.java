package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserPuzzleRepository extends CrudRepository<UserPuzzle, Long> {

  Optional<UserPuzzle> findByExternalKey(UUID externalKey);

  Optional<UserPuzzle> findByUserAndPuzzleExternalKey(User user, UUID puzzleExternalKey);

  Optional<UserPuzzle> findByUserAndPuzzle(User user, Puzzle puzzle);

  @Query("SELECT up FROM UserPuzzle as up WHERE up.user = :user AND up.puzzle = (SELECT p from Puzzle as p WHERE p.date = :date)")
  Optional<UserPuzzle> findByUserAndPuzzleDate(User user, LocalDate date);

  Iterable<UserPuzzle> findByUserOrderByCreatedDesc(User user);

  Iterable<UserPuzzle> findByPuzzleOrderByCreatedDesc(Puzzle puzzle);

  Optional<UserPuzzle> findById(long id);

//  Optional<UserPuzzle> findByUserAndUserPuzzle(User user, UserPuzzle puzzle);

  // Optional<UserPuzzle> findByUserAndDate(User user, Instant date);

  // Optional<List<UserPuzzle>> findByUserAndDateSince(User user, Instant date);


}
