package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import java.time.Instant;
import org.springframework.context.annotation.Profile;

@Profile({"service", "generate"})
public interface AbstractPuzzleService {

  Puzzle get(Instant date);

//  Puzzle getOrAddPuzzle(Instant date);

  Puzzle save(Puzzle puzzle);

  void createPuzzle();

}
