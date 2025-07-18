package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface PuzzleWordRepository extends CrudRepository<PuzzleWord, Long> {

  Optional<PuzzleWord> findByExternalKey(UUID externalKey);

}
