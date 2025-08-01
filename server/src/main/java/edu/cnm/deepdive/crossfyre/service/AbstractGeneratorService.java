package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle.Board;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import java.util.List;

public interface AbstractGeneratorService {

  // Have it return a List<PuzzleWords> with a Board
  List<PuzzleWord> generatePuzzleWords(Board frame);


}
