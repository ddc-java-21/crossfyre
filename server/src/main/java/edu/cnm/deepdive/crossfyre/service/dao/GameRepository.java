package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.Game;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Long> {

Optional<Game> findByUserAndUserPuzzle_Id(User author, long puzzle_id);

Iterable<Game> findByUserPuzzle_IdOrderByPostedAsc(long puzzle_id);
}
