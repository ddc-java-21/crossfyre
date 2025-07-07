package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.configuration.CrossfyreConfiguration;
import edu.cnm.deepdive.crossfyre.configuration.CrossfyreConfiguration.Polling;
import edu.cnm.deepdive.crossfyre.model.entity.SolutionPuzzle;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import edu.cnm.deepdive.crossfyre.model.entity.UserWord;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.service.dao.UserPuzzleRepository;
import edu.cnm.deepdive.crossfyre.service.dao.UserWordRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

@Service
@Profile("service")
public class GameService implements AbstractGameService {

  private static final List<UserWord> EMPTY_USER_WORD_LIST = List.of();

  private final UserWordRepository userWordRepository;
  private final UserPuzzleRepository userPuzzleRepository;
  private final ScheduledExecutorService scheduler;
  private final long pollingInterval;
  private final long pollingTimeout;

  @Autowired
  GameService(UserWordRepository userWordRepository, UserPuzzleRepository userPuzzleRepository,
      CrossfyreConfiguration configuration) {
    this.userWordRepository = userWordRepository;
    this.userPuzzleRepository = userPuzzleRepository;
    Polling polling = configuration.getPolling();
    scheduler = Executors.newScheduledThreadPool(polling.getPoolSize());
    pollingInterval = polling.getInterval().toMillis();
    pollingTimeout = polling.getTimeout().toMillis();
  }

  @Override
  public UserWord get(UserPuzzle puzzleKey, UUID wordKey) {
    return userPuzzleRepository
        .findByExternalKey(wordKey)
        .map((puzzle) -> userWordRepository.findByUserPuzzle(userPuzzleRepository.)

  }

  @Override
  public Iterable<UserWord> getAllInPuzzle(UUID puzzleKey) {
    return userPuzzleRepository
        .findByExternalKey(puzzleKey)
        .map(UserPuzzle::getUserWords)// TODO: 7/6/2025  map properly
        .orElseThrow();

  }

  @Override
  public DeferredResult<Iterable<UserWord>> getAllInPuzzleSince(UUID puzzleKey, Instant cutoff) {
    DeferredResult<Iterable<UserWord>> result =
        new DeferredResult<>(pollingInterval);
    ScheduledFuture<?>[] futurePolling = new ScheduledFuture[1];
    Runnable timeoutTask = () -> sendResult(futurePolling, result, EMPTY_USER_WORD_LIST);
    result.onTimeout(timeoutTask);
    UserPuzzle userPuzzle = userPuzzleRepository
        .findByExternalKey(puzzleKey)
        .orElseThrow();
    Runnable pollingTask = () -> checkForWords(cutoff, userPuzzle, futurePolling, result);
    futurePolling[0] =
        scheduler.scheduleAtFixedRate(pollingTask, 0, pollingInterval, TimeUnit.MILLISECONDS);
    return result;
  }

  @Override
  public UserWord add(User author, UUID puzzleKey, UserWord userWord) {
    return null;
  }

  @Override
  public UserWord get(UUID puzzleKey, UUID wordKey) {
    return null;
  }

  private void checkForWords(Instant created, UserPuzzle userPuzzle,
      ScheduledFuture<?>[] futurePolling,
      DeferredResult<Iterable<UserWord>> result) {
    userWordRepository
        .findByUserPuzzle(userPuzzle)
        .ifPresent((word) -> sendResult(futurePolling, result, ));
  }

  private void sendResult(ScheduledFuture<?>[] futurePolling,
      DeferredResult<Iterable<UserWord>> result, Iterable<UserWord> words) {
    futurePolling[0].cancel(true);
    result.setResult(words);
  }

  @Override
  public UserWord add(User author, UUID puzzleKey, UserWord userWord, SolutionPuzzle solutionPuzzle,
      UserPuzzle userPuzzle) {
    return userPuzzleRepository
        .findByExternalKey(puzzleKey)
        .map((puzzle) -> {
          userWord.setUserPuzzle(userPuzzle);
          puzzle.setSolutionPuzzle(solutionPuzzle);
          return userWordRepository.save(userWord);
        })
        .orElseThrow();
  }

}
