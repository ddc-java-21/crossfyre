package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface PuzzleWordRepository extends CrudRepository<PuzzleWord, Long> {

  Optional<PuzzleWord> findByExternalKey(UUID externalKey);

  Optional<PuzzleWord> findByPuzzleDateAndExternalKey(Instant date, UUID externalKey);

  Optional<PuzzleWord> findByPuzzleAndExternalKey(Puzzle puzzle, UUID externalKey);

  Iterable<PuzzleWord> findByPuzzleDate(Instant date);

  Iterable<PuzzleWord> findByPuzzle(Puzzle puzzle);

}
