package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface PuzzleWordRepository extends CrudRepository <PuzzleWord, Long> {

  Optional<PuzzleWord> findByPuzzle_PuzzleDate(Instant date);

  Iterable<PuzzleWord> findByPuzzleOrderByWordLengthDesc(Puzzle puzzle);

}
