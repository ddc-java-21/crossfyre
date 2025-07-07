package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface PuzzleRepository extends CrudRepository<UserPuzzle, Long> {

  Optional<UserPuzzle> findByExternalKey(UUID externalKey);

  Iterable<UserPuzzle> getAllByOrderByTitleAsc();

}
