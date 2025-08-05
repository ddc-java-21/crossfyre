package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Guess;
import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public interface AbstractGuessService {

  public Guess get(User requestor, LocalDate date, UUID guessKey);

  public Iterable<Guess> getAllInUserPuzzle(User requestor, LocalDate puzzleDate);

  public UserPuzzle add(User requestor, LocalDate puzzleDate, Guess guess);

  }
