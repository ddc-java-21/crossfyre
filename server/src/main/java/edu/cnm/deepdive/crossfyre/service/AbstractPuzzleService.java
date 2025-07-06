package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import java.util.UUID;

public interface AbstractPuzzleService {

  Iterable<Puzzle> getAll();

  Puzzle get(UUID key);

}
