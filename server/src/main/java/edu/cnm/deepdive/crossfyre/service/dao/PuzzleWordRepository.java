package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface PuzzleWordRepository extends CrudRepository<PuzzleWord, Long> {

  Optional<PuzzleWord> findByExternalKey(UUID externalKey);
  Optional<PuzzleWord> findById(long id);

  Optional<PuzzleWord> findByPuzzleDateAndExternalKey(LocalDate date, UUID externalKey);
  Optional<PuzzleWord> findBywordName(String wordName);

  Optional<PuzzleWord> findByPuzzleAndExternalKey(Puzzle puzzle, UUID externalKey);
  Optional<PuzzleWord> findByWordPosition_WordRowAndWordPosition_WordColumn(int wordRow, int wordColumn);

  Iterable<PuzzleWord> findByPuzzleDate(LocalDate date);

  Iterable<PuzzleWord> findByPuzzle(Puzzle puzzle);
  Optional<PuzzleWord> findByClue(String clue);

}
