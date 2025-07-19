package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Guess;
import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.service.dao.GuessRepository;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleRepository;
import edu.cnm.deepdive.crossfyre.service.dao.UserPuzzleRepository;
import edu.cnm.deepdive.crossfyre.service.dao.UserRepository;
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
  private final UserRepository userRepository;

  @Autowired
  GuessService(GuessRepository guessRepository, UserPuzzleRepository userPuzzleRepository,
      PuzzleRepository puzzleRepository, UserRepository userRepository) {
    this.guessRepository = guessRepository;
    this.userPuzzleRepository = userPuzzleRepository;
    this.puzzleRepository = puzzleRepository;
    this.userRepository = userRepository;
  }

  @Override
  public Iterable<Guess> getAllInUserPuzzle(UUID userPuzzleKey, User user, Instant date) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(user, date)
        .map(guessRepository::findByUserPuzzleOrderByCreatedAsc)
        .orElseThrow();
//        .findByExternalKey()
//        .map(guessRepository::findByUserPuzzleOrderByIdDesc)
//        .orElseThrow();
  }

  @Override
  public Guess add(UUID userPuzzleKey, User user, Instant date, Guess guess) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(user, date)
        .map((userPuzzle) -> {
          guess.setUserPuzzle(userPuzzle);
          return guessRepository.save(guess);
        })
        .orElseThrow();
  }

  @Override
  public Guess get(UUID userPuzzleKey, User user, Instant date, UUID guessKey) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(user, date)
        .flatMap((userPuzzle) -> guessRepository.findByUserPuzzleAndExternalKey(userPuzzle, guessKey))
        .orElseThrow();
  }

//  @Override
//  public Iterable<Guess> getAllInUserPuzzle(Instant puzzleDate) {
//    return null; // TODO: 7/18/25 CHANGE THIS!
//  }
//
//  @Override
//  public Guess add(User requestor, Instant puzzleDate, Guess guess) {
//    Puzzle puzzle = puzzleRepository
//        .findPuzzleByDate
//  }


}
