package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Guess;
import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.service.dao.GuessRepository;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleRepository;
import edu.cnm.deepdive.crossfyre.service.dao.UserPuzzleRepository;
import java.time.Instant;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("service")
public class GuessService implements AbstractGuessService {

  private final GuessRepository guessRepository;
  private final UserPuzzleRepository userPuzzleRepository;
  private final PuzzleRepository puzzleRepository;

  @Autowired
  GuessService(GuessRepository guessRepository, UserPuzzleRepository userPuzzleRepository,
      PuzzleRepository puzzleRepository) {
    this.guessRepository = guessRepository;
    this.userPuzzleRepository = userPuzzleRepository;
    this.puzzleRepository = puzzleRepository;
  }

  @Override
  public Iterable<Guess> getAllInUserPuzzle(UUID userPuzzleKey) {
    return userPuzzleRepository
        .findByExternalKey(userPuzzleKey)
        .map(guessRepository::findByUserPuzzleOrderByIdDesc)
        .orElseThrow();
  }

  @Override
  public Iterable<Guess> getAllInUserPuzzle(Instant puzzleDate) {
    return null; // TODO: 7/18/25 CHANGE THIS!
  }

  @Override
  public Guess add(User requestor, Instant puzzleDate, Guess guess) {
    Puzzle puzzle = puzzleRepository
        .findPuzzleByDate
  }


}
