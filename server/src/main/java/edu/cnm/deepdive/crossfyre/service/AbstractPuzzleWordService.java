package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord.Direction;
import java.time.Instant;
import java.util.UUID;

public interface AbstractPuzzleWordService {

  Iterable<PuzzleWord> getAllInPuzzle(Instant puzzleDate);

  PuzzleWord add(Instant puzzleDate, PuzzleWord word, String wordName, String wordClue);

  PuzzleWord get(Instant puzzleDate, int wordRow, int wordColumn, int wordLength, Direction direction);

}
