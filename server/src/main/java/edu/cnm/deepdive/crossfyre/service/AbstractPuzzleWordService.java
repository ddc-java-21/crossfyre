package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import java.time.Instant;
import java.util.UUID;

public interface AbstractPuzzleWordService {

  Iterable<PuzzleWord> getAllInPuzzle(Instant date);

  PuzzleWord add(Instant date, PuzzleWord puzzleWord);

  PuzzleWord get(Instant date, UUID puzzleWordKey);



}
