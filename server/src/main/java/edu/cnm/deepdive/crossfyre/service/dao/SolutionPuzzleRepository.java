package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.SolutionPuzzle;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface SolutionPuzzleRepository extends CrudRepository<SolutionPuzzle, Long> {

  Optional<SolutionPuzzle> findByExternalKey(UUID externalKey);
  Iterable<SolutionPuzzle> findAllByOrderByTitleAsc();


}
