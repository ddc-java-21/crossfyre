package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface PuzzleRepository extends CrudRepository<Puzzle, Long> {

  Optional<Puzzle> findByExternalKey(UUID externalKey);

  Iterable<Puzzle> getAllByOrderByTitleAsc();

}
