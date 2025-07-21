package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserPuzzleRepository extends CrudRepository<UserPuzzle, Long> {

// Query to find a userpuzzle from first by   
  @Query("SELECT up FROM UserPuzzle as up WHERE up.user = :user AND up.puzzle = (SELECT p from Puzzle as p WHERE p.puzzleDate = :date)")
  Optional<UserPuzzle> findByUserAndPuzzleDate(User user, Instant date);

  Iterable<UserPuzzle> findByUserAndPuzzleOrderByCreatedAsc(User user, Puzzle puzzle);

}
