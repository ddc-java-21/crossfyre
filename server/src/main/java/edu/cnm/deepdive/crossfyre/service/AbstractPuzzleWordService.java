package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public interface AbstractPuzzleWordService {

  Iterable<PuzzleWord> getAllInPuzzle(LocalDate date);

  PuzzleWord add(LocalDate date, PuzzleWord puzzleWord);

  PuzzleWord get(LocalDate date, UUID puzzleWordKey);



}
