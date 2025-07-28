package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleRepository;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleWordRepository;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("service")
public class PuzzleService implements AbstractPuzzleService {

  private final PuzzleRepository puzzleRepository;
  private final PuzzleWordRepository puzzleWordRepository;

  @Autowired
  PuzzleService(PuzzleRepository puzzleRepository, PuzzleWordRepository puzzleWordRepository) {
    this.puzzleRepository = puzzleRepository;
    this.puzzleWordRepository = puzzleWordRepository;
  }

  @Override
  public Puzzle get(Instant date) {
    return puzzleRepository
        .findByDate(date)
        .orElseThrow();
  }

//  @Override
//  public Puzzle getOrAddPuzzle(Puzzle puzzle) {
//    return puzzleRepository
//        .findByDate(puzzle.getDate())
//        .or(() -> {
//
//          userPuzzle.setPuzzle(puzzle);
//          userPuzzle.setGuesses(new ArrayList<>());
//          userPuzzle.setSolved(false);
//          userPuzzleRepository.save(userPuzzle);
//          return Optional.of(userPuzzle);
//        })
//        .orElseThrow();
//  }

//  @Override
//  public

}
