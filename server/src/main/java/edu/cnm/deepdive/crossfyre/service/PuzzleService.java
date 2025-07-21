package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleRepository;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("service")
public class PuzzleService implements AbstractPuzzleService {

  private final PuzzleRepository puzzleRepository;

  @Autowired
  PuzzleService(PuzzleRepository puzzleRepository) {
    this.puzzleRepository = puzzleRepository;
  }

  @Override
  public Puzzle get(Instant date) {
    return puzzleRepository
        .findByDate(date)
        .orElseThrow();
  }

}
