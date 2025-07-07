package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.SolutionWord;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface SolutionWordRepository extends CrudRepository<SolutionWord, Long> {

  Optional<SolutionWord> findBySolutionPuzzle_IdAndClue(long solutionPuzzleId);

  Iterable<SolutionWord> findAllByOrderByWordNameAsc();

}
