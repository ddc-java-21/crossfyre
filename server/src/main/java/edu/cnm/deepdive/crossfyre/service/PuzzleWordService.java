package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleRepository;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleWordRepository;
import java.time.Instant;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("service")
public class PuzzleWordService implements AbstractPuzzleWordService {

  private final PuzzleWordRepository puzzleWordRepository;
  private final PuzzleRepository puzzleRepository;

  @Autowired
  PuzzleWordService(PuzzleWordRepository puzzleWordRepository, PuzzleRepository puzzleRepository) {
    this.puzzleWordRepository = puzzleWordRepository;
    this.puzzleRepository = puzzleRepository;
  }

  @Override
  public Iterable<PuzzleWord> getAllInPuzzle(Instant date) {
    return puzzleRepository
        .findByDate(date)
        .map(puzzleWordRepository::findByPuzzle)
        .orElseThrow();
  }

  @Override
  public PuzzleWord add(Instant date, PuzzleWord puzzleWord) {
    return puzzleRepository
        .findByDate(date)
        .map((puzzle) -> {
          puzzleWord.setPuzzle(puzzle);
          return puzzleWordRepository.save(puzzleWord);
        })
        .orElseThrow();
  }

  @Override
  public PuzzleWord get(Instant date, UUID puzzleWordKey) {
    return puzzleRepository
        .findByDate(date)
        .flatMap((puzzle) -> puzzleWordRepository.findByPuzzleDateAndExternalKey(date, puzzleWordKey))
        .orElseThrow();
  }

}
