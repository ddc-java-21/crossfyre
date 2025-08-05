package edu.cnm.deepdive.crossfyre.viewmodel;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.cnm.deepdive.crossfyre.model.dto.User;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzleDto;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzleDto.Guess;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzleDto.Puzzle;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzleDto.Puzzle.PuzzleWord;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzleDto.Puzzle.PuzzleWord.Direction;
import edu.cnm.deepdive.crossfyre.service.CrossfyreService;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.inject.Inject;

@HiltViewModel
public class PuzzleViewModel extends ViewModel implements DefaultLifecycleObserver {

  private static final String TAG = PuzzleViewModel.class.getSimpleName();

  private final CrossfyreService crossfyreService;
  private final MutableLiveData<User> currentUser = new MutableLiveData<>();
  private final MutableLiveData<UserPuzzleDto> userPuzzle;
  private final LiveData<UserPuzzleDto.Puzzle> currentPuzzle;
  private final LiveData<int[]> wordStarts;
  private final LiveData<List<UserPuzzleDto.Puzzle.PuzzleWord>> words;
  private final MutableLiveData<UserPuzzleDto.Puzzle.PuzzleWord> selectedWord;
  private final MutableLiveData<List<Integer>> selectedCellPositions;
  private final LiveData<UserPuzzleDto.Puzzle.PuzzleWord.Direction> selectedDirection;
  private final LiveData<List<UserPuzzleDto.Guess>> guesses;
  private final MutableLiveData<Integer> selectedSquare;
  private final LiveData<boolean[][]> grid;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  private int lastClickedRow = -1;
  private int lastClickedCol = -1;
  private UserPuzzleDto.Puzzle.PuzzleWord.Direction lastDirection = null;

  // Stretch goal boolean[][] false = wall, true = space because grid below is just getting board

  private static Character[][] buildBoard(UserPuzzleDto up) {
    if (up == null || up.getPuzzle() == null) {
      return null;
    }
    int size = up.getPuzzle().getSize();
    Character[][] grid = new Character[size][size];
    String layout = up.getPuzzle().getBoard().day;
    if (layout.length() != size * size) {
      throw new IllegalStateException("Board layout does not match size × size.");
    }
    int index = 0;
    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        grid[row][col] = layout.charAt(index++);
      }
    }
    return grid;
  }
  // LiveData for mapping position → clue number

  @Inject
  public PuzzleViewModel(CrossfyreService crossfyreService) {
    this.crossfyreService = crossfyreService;
    userPuzzle = new MutableLiveData<>();
    currentPuzzle = Transformations.map(userPuzzle, (up) -> (up != null) ? up.getPuzzle() : null);
    wordStarts = Transformations.map(userPuzzle, (up) -> (up != null) ? up.getPuzzle()
        .getWordStarts() : null);
    words = Transformations.map(currentPuzzle, (cp) -> (cp != null) ? cp.getPuzzleWords() : null);
    selectedWord = new MutableLiveData<>();
    selectedCellPositions = new MutableLiveData<>();
    selectedDirection = Transformations.map(selectedWord,
        (sw) -> (sw != null) ? sw.getDirection() : null);
    guesses = Transformations.map(userPuzzle, (up) -> (up != null) ? up.getGuesses() : null);
    selectedSquare = new MutableLiveData<>();
    pending = new CompositeDisposable();
    throwable = new MutableLiveData<>();
    grid = Transformations.map(currentPuzzle, Puzzle::getGrid);
    fetchCurrentUser();
    fetchUserPuzzle();
  }

  // Server side check to use Schedules single threaded pool method instead of Schedules.io()
  private void fetchCurrentUser() {
    throwable.setValue(null);
    crossfyreService.getMyProfile()
        .subscribe(
            currentUser::postValue,
            this::postThrowable,
            pending
        );

  }

  private void fetchUserPuzzle() {
    throwable.setValue(null);
    crossfyreService.getUserPuzzle(LocalDate.now())
        .subscribe(
            this::handleUserPuzzle,
            this::postThrowable,
            pending
        );
  }

  private void handleUserPuzzle(UserPuzzleDto dto) {
    if (dto != null) {
      Log.d(TAG, "Board layout: " + dto.getPuzzle().getBoard().day);
      Log.d(TAG, "Puzzle words count: " + dto.getPuzzle().getPuzzleWords().size());
      userPuzzle.postValue(dto);
    }
  }

  public void selectSquare(int position) {
    UserPuzzleDto.Puzzle puzzle = userPuzzle.getValue().getPuzzle();

    int row = position / puzzle.getSize();
    int col = position % puzzle.getSize();
    boolean sameCellClicked = lastClickedRow == row && lastClickedCol == col;

    //noinspection SimplifyStreamApiCallChains
    List<PuzzleWord> candidates = puzzle
        .getPuzzleWords()
        .stream()
        .filter((word) -> word.includes(row, col))
        .collect(Collectors.toList());

    if (candidates.isEmpty()) {
      return;
    }

    PuzzleWord matchedWord = (candidates.size() == 1)
        ? candidates.get(0)
        : candidates
            .stream()
            .filter((word) -> sameCellClicked
                ? !word.equals(selectedWord.getValue())
                : word.getDirection() == Direction.ACROSS
            )
            .findFirst()
            .orElseThrow();

    lastClickedRow = row;
    lastClickedCol = col;
    lastDirection = matchedWord.getDirection();

    int startRow = matchedWord.getWordPosition().getRow();
    int startCol = matchedWord.getWordPosition().getColumn();
    int size = puzzle.getSize();
    int rowOffset = matchedWord.getDirection().rowOffset();
    int colOffset = matchedWord.getDirection().columnOffset();
    List<Integer> selectedPositions = IntStream
        .range(0, matchedWord.getWordPosition().getLength())
        .map((sp) -> {
          int selectionRow = startRow + sp * rowOffset;
          int selectionCol = startCol + sp * colOffset;
          return selectionRow * size + selectionCol;
        })
        .boxed()
        .collect(Collectors.toList());

    selectedCellPositions.setValue(selectedPositions);
    selectedSquare.setValue(position);
    selectedWord.setValue(matchedWord);
  }


  public void sendGuess(Guess guess) {
    throwable.setValue(null);
    crossfyreService.sendGuess(
            userPuzzle.getValue().getPuzzle().getDate(),
            guess
        )
        .subscribe(
            userPuzzle::postValue,
            this::postThrowable,
            pending
        );
  }

  public void selectWord(PuzzleWord word) {
    selectedWord.setValue(word);
  }

  @Override
  public void onStop(@NonNull LifecycleOwner owner) {
    pending.clear();
    DefaultLifecycleObserver.super.onStop(owner);
  }

  private void postThrowable(Throwable throwable) {
    Log.e(TAG, throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }


  // LiveData Getters
  public LiveData<boolean[][]> getGrid() {
    return grid;
  }

  public LiveData<int[]> getWordStarts() {
    return wordStarts;
  }

  public LiveData<UserPuzzleDto.Puzzle.PuzzleWord> getSelectedWord() {
    return selectedWord;
  }

  public LiveData<List<Integer>> getSelectedCellPositions() {
    return selectedCellPositions;
  }

  public LiveData<UserPuzzleDto.Puzzle.PuzzleWord.Direction> getSelectedDirection() {
    return selectedDirection;
  }

  public LiveData<List<UserPuzzleDto.Guess>> getGuesses() {
    return guesses;
  }

  public LiveData<Integer> getSelectedSquare() {
    return selectedSquare;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public LiveData<UserPuzzleDto> getUserPuzzle() {
    return userPuzzle;
  }

  public LiveData<UserPuzzleDto.Puzzle> getCurrentPuzzle() {
    return currentPuzzle;
  }

}