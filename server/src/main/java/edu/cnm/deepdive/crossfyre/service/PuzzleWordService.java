package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord.Direction;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleRepository;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleWordRepository;
import java.time.Instant;
import java.util.UUID;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("service")
public class PuzzleWordService implements AbstractPuzzleWordService {

  private final PuzzleRepository puzzleRepository;
  private final PuzzleWordRepository puzzleWordRepository;

  public PuzzleWordService(PuzzleRepository puzzleRepository,
      PuzzleWordRepository puzzleWordRepository) {
    this.puzzleRepository = puzzleRepository;
    this.puzzleWordRepository = puzzleWordRepository;
  }


  @Override
  public Iterable<PuzzleWord> getAllInPuzzle(Instant puzzleDate) {
    return puzzleRepository
        .findByPuzzleDate(puzzleDate)
        .map(puzzleWordRepository::findByPuzzleOrderByWordLengthDesc)
        .orElseThrow();
  }

  @Override
  public PuzzleWord add(Instant puzzleDate, PuzzleWord word, String wordName, String wordClue) {
    return puzzleRepository
        .findByPuzzleDate(puzzleDate)
        .map((puzzle) -> {
          word.setWordName(wordName);
          word.setClue(wordClue);
          word.setPuzzle(puzzle);
          return puzzleWordRepository.save(word);
        })
        .orElseThrow();
  }

  @Override
  public PuzzleWord get(Instant puzzleDate, int wordRow, int wordColumn, int wordLength, Direction direction) {
    return puzzleRepository
        .findByPuzzleDate(puzzleDate)
        .flatMap((puzzle) -> puzzleWordRepository.findByPuzzle_PuzzleDate(puzzleDate))
        .orElseThrow();
  }
}
