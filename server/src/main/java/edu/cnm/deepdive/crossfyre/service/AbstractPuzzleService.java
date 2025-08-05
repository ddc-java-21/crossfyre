package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import java.time.Instant;
import java.time.LocalDate;

public interface AbstractPuzzleService {

  Puzzle get(LocalDate date);

//  Puzzle getOrAddPuzzle(Instant date);

}
