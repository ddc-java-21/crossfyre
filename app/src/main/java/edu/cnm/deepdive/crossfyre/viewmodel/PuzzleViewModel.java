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
import edu.cnm.deepdive.crossfyre.model.dto.GuessDto;
import edu.cnm.deepdive.crossfyre.model.dto.PuzzleWord;
import edu.cnm.deepdive.crossfyre.model.dto.PuzzleWord.Direction;
import edu.cnm.deepdive.crossfyre.model.dto.User;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzle.Guess.Puzzle;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzleDto;
import edu.cnm.deepdive.crossfyre.service.CrossfyreService;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

@HiltViewModel
public class PuzzleViewModel extends ViewModel implements DefaultLifecycleObserver {

  private static final String TAG = PuzzleViewModel.class.getSimpleName();
  private final CrossfyreService crossfyreService;
  private final MutableLiveData<User> currentUser;

  private final MutableLiveData<Puzzle> currentPuzzle = new MutableLiveData<>();
  private final MutableLiveData<Puzzle.PuzzleWord> selectedWord = new MutableLiveData<>();
  private final MutableLiveData<List<Integer>> selectedCellPosition = new MutableLiveData<>();
  private final MutableLiveData<List<PuzzleWord>> words = new MutableLiveData<>(new ArrayList<>());
  private final MutableLiveData<UserPuzzleDto> userPuzzle = new MutableLiveData<>();
  private final MutableLiveData<Direction> selectedDirection = new MutableLiveData<>(
      Direction.ACROSS);

  private final MutableLiveData<List<GuessDto>> guesses;
  private final MutableLiveData<GuessDto> selectedSquare;

  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  private Disposable description;


  private Integer lastClickedRow = null;
  private Integer lastClickedCol = null;
  private Puzzle.PuzzleWord.Direction lastDirection = null;

  // LiveData for board (grid of characters)
  private final LiveData<Character[][]> board = Transformations.map(userPuzzle, up -> {
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
  });

  // LiveData for mapping position → clue number
  private final LiveData<Map<Integer, Integer>> wordStartMap =
      Transformations.map(userPuzzle, up -> {
        Map<Integer, Integer> map = new HashMap<>();
        if (up == null || up.getPuzzle().getPuzzleWords() == null) {
          return map;
        }
        int size = up.getPuzzle().getSize();
        int clueNumber = 1;
        for (UserPuzzleDto.Puzzle.PuzzleWord word : up.getPuzzle().getPuzzleWords()) {
          int row = word.getWordPosition().getRow();
          int col = word.getWordPosition().getColumn();
          int flatIndex = row * size + col;
          // Share the same number if across & down start at same cell
          if (!map.containsKey(flatIndex)) {
            map.put(flatIndex, clueNumber++);
          }
        }
        return map;
      });

  public LiveData<Character[][]> getBoard() {
    return board;
  }


  public MutableLiveData<List<GuessDto>> getGuesses() {
    return guesses;
  }

  public MutableLiveData<UserPuzzleDto> getUserPuzzle() {
    return userPuzzle;
  }

  public MutableLiveData<GuessDto> getSelectedSquare() {
    return selectedSquare;
  }

  @Inject
  public PuzzleViewModel(CrossfyreService crossfyreService) {
    this.crossfyreService = crossfyreService;
    currentUser = new MutableLiveData<>();
    guesses = new MutableLiveData<>(new ArrayList<>());
    selectedSquare = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    fetchCurrentUser();
    fetchUserPuzzle();
  }

  private void fetchCurrentUser() {
    throwable.setValue(null);
    crossfyreService
        .getMyProfile()
        .subscribe(
            currentUser::postValue,
            this::postThrowable,
            pending
        );
  }

  public void selectSquare(int position) {
    Puzzle puzzle = currentPuzzle.getValue();
    if (puzzle == null) {
      return;
    }

    int row = position / puzzle.getSize();
    int col = position % puzzle.getSize();

    boolean sameCellClicked =
        lastClickedRow != null && lastClickedCol != null &&
            lastClickedRow == row && lastClickedCol == col;

    Puzzle.PuzzleWord.Direction desiredDirection;
    if (sameCellClicked) {
      desiredDirection = (lastDirection == Puzzle.PuzzleWord.Direction.ACROSS)
          ? Puzzle.PuzzleWord.Direction.DOWN
          : Puzzle.PuzzleWord.Direction.ACROSS;
    } else {
      desiredDirection = Puzzle.PuzzleWord.Direction.ACROSS;
    }

    Puzzle.PuzzleWord matchedWord = null;
    for (Puzzle.PuzzleWord word : puzzle.getPuzzleWords()) {
      int startRow = word.getWordPosition().getRow();
      int startCol = word.getWordPosition().getColumn();
      int length = word.getWordPosition().getLength();

      if (word.getDirection() == Puzzle.PuzzleWord.Direction.ACROSS) {
        if (row == startRow && col >= startCol && col < startCol + length) {
          matchedWord = word;
          break;
        }
      } else if (word.getDirection() == Puzzle.PuzzleWord.Direction.DOWN) {
        if (col == startCol && row >= startRow && row < startRow + length) {
          matchedWord = word;
          break;
        }
      }
    }

    if (matchedWord != null) {
      selectedWord.setValue(matchedWord);
      lastClickedRow = row;
      lastClickedCol = col;
      lastDirection = matchedWord.getDirection();

      List<Integer> matchedPositions = new ArrayList<>();
      int startRow = matchedWord.getWordPosition().getRow();
      int startCol = matchedWord.getWordPosition().getColumn();
      int length = matchedWord.getWordPosition().getLength();

      if (matchedWord.getDirection() == Puzzle.PuzzleWord.Direction.ACROSS) {
        for (int offset = 0; offset < length; offset++) {
          matchedPositions.add(startRow * puzzle.getSize() + (startCol + offset));
        }
      } else {
        for (int offset = 0; offset < length; offset++) {
          matchedPositions.add((startRow + offset) * puzzle.getSize() + startCol);
        }
      }
      selectedCellPosition.setValue(matchedPositions);
    }
  }

  // Need methods that the UI controller can invoke when the user clicks so that it knows which
  // puzzleword the user is looking at by new word or orientation switch

  private void fetchUserPuzzle() {
    throwable.setValue(null);
    crossfyreService
        .getUserPuzzle(Instant.now().truncatedTo(ChronoUnit.DAYS))
        .subscribe(
            userPuzzle::postValue,
            this::postThrowable,
            pending
        );
  }

  public void sendGuess(GuessDto guess) {
    throwable.setValue(null);
    //noinspection DataFlowIssue
    crossfyreService
        .sendGuess(userPuzzle.getValue().getPuzzle().getDate(), selectedSquare.getValue())
        .subscribe(
            guesses::postValue,
            // TODO: 8/2/25  here, you need to do a GET to check the state of the puzzle
            this::postThrowable,
            pending
        );
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


  public LiveData<List<PuzzleWord>> getWords() {
    return words;
  }

  public LiveData<Map<Integer, Integer>> getWordStartMap() {
    return wordStartMap;
  }

  // Will let you know the direction anyway
  public LiveData<Puzzle.PuzzleWord> getSelectedWord() {
    return selectedWord;
  }
  // UI logic will never set the words

  public LiveData<List<Integer>> getSelectedCellPosition() {
    return selectedCellPosition;
  }

  public void setPuzzle(Puzzle puzzle) {
    currentPuzzle.setValue(puzzle);
  }


}
