package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Guess;
import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import edu.cnm.deepdive.crossfyre.service.dao.GuessRepository;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleRepository;
import edu.cnm.deepdive.crossfyre.service.dao.UserPuzzleRepository;
import java.time.Instant;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  public Guess get(User user, Instant date, UUID guessKey) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(user, date)
        .flatMap((userPuzzle) -> guessRepository.findByUserPuzzleAndExternalKey(userPuzzle, guessKey))
        .orElseThrow();
  }

  @Override
  public Iterable<Guess> getAllInUserPuzzle(User requestor, Instant puzzleDate) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(requestor, puzzleDate)
        .map(userPuzzle -> {
          return guessRepository.findByUserPuzzleOrderByIdAsc(userPuzzle);
        })
        .orElseThrow();
  }

  @Override
  public Guess add(User requestor, Puzzle puzzle, Guess guess) {
    return userPuzzleRepository
        .findByUserAndPuzzle(requestor, puzzle)
        .map((userPuzzle) -> {
          guess.setUserPuzzle(userPuzzle);
          return guessRepository.save(guess);
        })
        .orElseThrow(); // custom exception goes here
  }

  @Override
//  @Transactional
  public synchronized UserPuzzle add(User requestor, Instant puzzleDate, Guess guess) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(requestor, puzzleDate)
        .flatMap((userPuzzle) -> {
          guess.setUserPuzzle(userPuzzle);
          guessRepository.save(guess);
          return userPuzzleRepository.findByUserAndPuzzleDate(requestor, puzzleDate);
        })
        .orElseThrow(); // custom exception goes here
  }

//  @Override
//  public Guess add(User requestor, Instant puzzleDate, Guess guess) {
//    return userPuzzleRepository
//        .findByUserAndPuzzleDate(requestor, puzzleDate)
//        .map((userPuzzle) -> {
//          guess.setUserPuzzle(userPuzzle);
//          return guessRepository.save(guess);
//        })
//        .orElseThrow(); // custom exception goes here
//  }


}
