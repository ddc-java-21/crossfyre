package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Guess;
import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import java.time.Instant;
import java.util.UUID;

public interface AbstractGuessService {

  public Guess get(User requestor, Instant date, UUID guessKey);

  public Iterable<Guess> getAllInUserPuzzle(User requestor, Instant puzzleDate);

  public Guess add(User requestor, Puzzle puzzle, Guess guess);

  public UserPuzzle add(User requestor, Instant puzzleDate, Guess guess);

  }
