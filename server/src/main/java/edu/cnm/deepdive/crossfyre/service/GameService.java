package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.configuration.CrossfyreConfiguration;
import edu.cnm.deepdive.crossfyre.configuration.CrossfyreConfiguration.Polling;
import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.Word;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.model.entity.Word;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleRepository;
import edu.cnm.deepdive.crossfyre.service.dao.WordRepository;
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

  private static final List<Word> EMPTY_WORD_LIST = List.of();

  private final WordRepository wordRepository;
  private final PuzzleRepository puzzleRepository;
  private final ScheduledExecutorService scheduler;
  private final long pollingInterval;
  private final long pollingTimeout;

  @Autowired
  GameService(WordRepository wordRepository, PuzzleRepository channelRepository, CrossfyreConfiguration configuration) {
    this.wordRepository = wordRepository;
    this.puzzleRepository = puzzleRepository;
    Polling polling = configuration.getPolling();
    scheduler = Executors.newScheduledThreadPool(polling.getPoolSize());
    pollingInterval = polling.getInterval().toMillis();
    pollingTimeout = polling.getTimeout().toMillis();
  }

  @Override
  public Word get(UUID puzzleKey, UUID wordKey) {
    return puzzleRepository
        .findByExternalKey(puzzleKey)
        .flatMap((Puzzle) -> wordRepository.findByPuzzleAndExternalKey(puzzle, wordKey))
        .orElseThrow();
  }

  @Override
  public Iterable<Word> getAllInPuzzle(UUID puzzleKey) {
    return puzzleRepository
        .findByExternalKey(puzzleKey)
        .map((Puzzle puzzle) -> wordRepository.findByPuzzleOrderByPostedAsc(puzzle))// TODO: 7/6/2025  map properly
        .orElseThrow();
  }

  @Override
  public DeferredResult<Iterable<Word>> getAllInPuzzleSince(UUID puzzleKey, Instant cutoff) {
    DeferredResult<Iterable<Word>> result =
        new DeferredResult<>(pollingInterval);
    ScheduledFuture<?>[] futurePolling = new ScheduledFuture[1];
    Runnable timeoutTask = () -> sendResult(futurePolling, result, EMPTY_WORD_LIST);
    result.onTimeout(timeoutTask);
      Puzzle puzzle = puzzleRepository
          .findByExternalKey(puzzleKey)
          .orElseThrow();
    Runnable pollingTask = () -> checkForWords(cutoff, puzzle, futurePolling, result);
    futurePolling[0] =
        scheduler.scheduleAtFixedRate(pollingTask, 0, pollingInterval, TimeUnit.MILLISECONDS);
   return result;
  }

  private void checkForWords(Instant cutoff, Puzzle puzzle, ScheduledFuture<?>[] futurePolling,
      DeferredResult<Iterable<Word>> result) {
    wordRepository
        .findFirstByPuzzleAndPostedAfterOrderByPostedDesc(puzzle, cutoff)
        .ifPresent((posted) -> sendResult(futurePolling, result,
            wordRepository.findByPuzzleAndPostedAfterOrderByPostedAsc(puzzle, cutoff)));
  }

  private void sendResult(ScheduledFuture<?>[] futurePolling,
      DeferredResult<Iterable<Word>> result, Iterable<Word> words) {
    futurePolling[0].cancel(true);
    result.setResult(
        word);
  }

  @Override
  public Word add(User author, UUID puzzleKey, Word word) {
    return puzzleRepository
        .findByExternalKey(puzzleKey)
        .map((puzzle) -> {
          word.setAuthor(author);
          puzzle.setPuzzle(puzzle);
          return wordRepository.save(word);
        })
        .orElseThrow();
  }

}
