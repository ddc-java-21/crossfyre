package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import java.time.Instant;
import java.util.UUID;

public interface AbstractPuzzleService {

  Puzzle get(Instant date);

}
