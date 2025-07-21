package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Guess;
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

  @Autowired
  GuessService(GuessRepository guessRepository, UserPuzzleRepository userPuzzleRepository) {
    this.guessRepository = guessRepository;
    this.userPuzzleRepository = userPuzzleRepository;
  }

  @Override
  public Iterable<Guess> getAllInUserPuzzle(User user, Instant date) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(user, date)
        .map(guessRepository::findByUserPuzzleOrderByCreatedAsc)
        .orElseThrow();
//        .findByExternalKey()
//        .map(guessRepository::findByUserPuzzleOrderByIdDesc)
//        .orElseThrow();
  }

  @Override
  public Guess add(User user, Instant date, Guess guess) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(user, date)
        .map((userPuzzle) -> {
          guess.setUserPuzzle(userPuzzle);
          return guessRepository.save(guess);
        })
        .orElseThrow();
  }

  @Override
  public Guess get(User user, Instant date) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(user, date)
        .flatMap(guessRepository::findByUserPuzzle)
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
