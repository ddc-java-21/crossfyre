package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.Puzzle.Board;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleRepository;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleWordRepository;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Profile("service")
public class PuzzleService implements AbstractPuzzleService {

  private final PuzzleRepository puzzleRepository;
  private final PuzzleWordRepository puzzleWordRepository;

  private static final long PUZZLE_CREATION_POLLING_INTERVAL = 30_000;
  private static final long PUZZLE_CREATION_IDLE_TIMEOUT = 90_000;
  private final AbstractPuzzleService abstractPuzzleService;
  private final PuzzleWordService puzzleWordService;


  @Autowired
  PuzzleService(PuzzleRepository puzzleRepository, PuzzleWordRepository puzzleWordRepository,
      AbstractPuzzleService abstractPuzzleService, PuzzleWordService puzzleWordService) {
    this.puzzleRepository = puzzleRepository;
    this.puzzleWordRepository = puzzleWordRepository;
    this.abstractPuzzleService = abstractPuzzleService;
    this.puzzleWordService = puzzleWordService;
  }


  // At 0:00 of every day Mon-Sun this method will invoke.
  // We need to get a new instance of the Puzzle object.
  // We need to get the size of the puzzle, assign the correct board layout for the puzzle day,
  //assign the correct date to the puzzle, and get the List<PuzzleWords> for that puzzle.
  @Scheduled(cron = "0 0 0 * * *") // Runs every day at midnight
  public void createPuzzle() {

    LocalDate today = LocalDate.now();
    int currentDayValue = today.getDayOfWeek().getValue(); // 1 = Monday ... 7 = Sunday

    // Map Monday-Sunday (1-7) to index in Board enum where index 0 = SUNDAY
    int boardIndex = currentDayValue % 7; // Sunday (7) → 0

    Board[] boards = Board.values();
    Board todaysBoard = boards[boardIndex];

    Instant date = today.atStartOfDay(ZoneOffset.UTC).toInstant();

    // Create the Puzzle
    Puzzle puzzle = new Puzzle();
    puzzle.setBoard(todaysBoard);
    puzzle.setDate(date);
    puzzle.setSize(5);
    puzzle.setCreated(Instant.now());

    // Save puzzle first so it gets an ID
    puzzle = puzzleRepository.save(puzzle);

    // Fetch puzzle words and associate them with this puzzle
    Iterable<PuzzleWord> puzzleWords = puzzleWordService.getAllInPuzzle(date);
    for (PuzzleWord word : puzzleWords) {
      word.setPuzzle(puzzle);
      puzzleWordRepository.save(word);
    }
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
