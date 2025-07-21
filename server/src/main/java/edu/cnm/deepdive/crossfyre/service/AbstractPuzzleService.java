package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import java.time.Instant;

public interface AbstractPuzzleService {

  Iterable<Puzzle> getAll();

  Puzzle add(Puzzle puzzle);

  Puzzle get(Instant date);
}
