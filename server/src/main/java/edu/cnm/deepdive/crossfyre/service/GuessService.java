package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Guess;
import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import edu.cnm.deepdive.crossfyre.service.dao.GuessRepository;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleRepository;
import edu.cnm.deepdive.crossfyre.service.dao.UserPuzzleRepository;
import java.time.Instant;
import java.time.LocalDate;
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

  @Autowired
  GuessService(GuessRepository guessRepository, UserPuzzleRepository userPuzzleRepository) {
    this.guessRepository = guessRepository;
    this.userPuzzleRepository = userPuzzleRepository;
  }

  @Override
  public Guess get(User user, LocalDate date, UUID guessKey) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(user, date)
        .flatMap((userPuzzle) -> guessRepository.findByUserPuzzleAndExternalKey(userPuzzle, guessKey))
        .orElseThrow();
  }

  @Override
  public Iterable<Guess> getAllInUserPuzzle(User requestor, LocalDate puzzleDate) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(requestor, puzzleDate)
        .map(guessRepository::findByUserPuzzleOrderByIdAsc)
        .orElseThrow();
  }

  @Override
  @Transactional
  public UserPuzzle add(User requestor, LocalDate puzzleDate, Guess guess) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(requestor, puzzleDate)
        .flatMap((userPuzzle) -> {
          for (Guess previousGuess : userPuzzle.getGuesses()) {
            if (previousGuess.getGuessPosition().guessRow() == guess.getGuessPosition().guessRow()
                && previousGuess.getGuessPosition().guessColumn() == guess.getGuessPosition().guessColumn()) {
              guessRepository.delete(previousGuess);
            }
          }
          //userPuzzle.getGuesses().add(guess);
          userPuzzleRepository.save(userPuzzle);
          return userPuzzleRepository.findByUserAndPuzzleDate(requestor, puzzleDate);
        })
        .orElseThrow(); // custom exception goes here
  }

}
