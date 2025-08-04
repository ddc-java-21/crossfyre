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
  private final MutableLiveData<User> currentUser = new MutableLiveData<>();
  private final MutableLiveData<UserPuzzleDto> userPuzzle = new MutableLiveData<>();
  private final MutableLiveData<UserPuzzleDto.Puzzle> currentPuzzle;
  private final MutableLiveData<List<UserPuzzleDto.Puzzle.PuzzleWord>> words = new LiveData<>();
  private final MutableLiveData<UserPuzzleDto.Puzzle.PuzzleWord> selectedWord = new MutableLiveData<>();
  private final MutableLiveData<List<Integer>> selectedCellPositions = new MutableLiveData<>();
  private final MutableLiveData<UserPuzzleDto.Puzzle.PuzzleWord.Direction> selectedDirection = new MutableLiveData<>(UserPuzzleDto.Puzzle.PuzzleWord.Direction.ACROSS);
  private final MutableLiveData<List<UserPuzzleDto.Guess>> guesses = new MutableLiveData<>();
  private final MutableLiveData<GuessDto> selectedSquare = new MutableLiveData<>();
  private final MutableLiveData<Throwable> throwable = new MutableLiveData<>();
  private final CompositeDisposable pending = new CompositeDisposable();

  private Integer lastClickedRow = null;
  private Integer lastClickedCol = null;
  private UserPuzzleDto.Puzzle.PuzzleWord.Direction lastDirection = null;

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
  private final LiveData<Map<Integer, Integer>> wordStartMap = Transformations.map(userPuzzle, up -> {
    Map<Integer, Integer> map = new HashMap<>();
    if (up == null || up.getPuzzle() == null || up.getPuzzle().getPuzzleWords() == null) {
      return map;
    }
    int size = up.getPuzzle().getSize();
    int clueNumber = 1;
    for (UserPuzzleDto.Puzzle.PuzzleWord word : up.getPuzzle().getPuzzleWords()) {
      int row = word.getWordPosition().getRow();
      int col = word.getWordPosition().getColumn();
      int flatIndex = row * size + col;
      if (!map.containsKey(flatIndex)) {
        map.put(flatIndex, clueNumber++);
      }
    }
    return map;
  });

  @Inject
  public PuzzleViewModel(CrossfyreService crossfyreService,
      MutableLiveData<UserPuzzleDto.Puzzle> currentPuzzle) {
    this.crossfyreService = crossfyreService;
    currentPuzzle = Transformations.map()
    fetchCurrentUser();
    fetchUserPuzzle();
  }

  private void fetchCurrentUser() {
    throwable.setValue(null);
    pending.add(
        crossfyreService.getMyProfile()
            .subscribe(
                currentUser::postValue,
                this::postThrowable
            )
    );
  }

  private void fetchUserPuzzle() {
    throwable.setValue(null);
    pending.add(
        crossfyreService.getUserPuzzle(Instant.now().truncatedTo(ChronoUnit.DAYS))
            .subscribe(
                dto -> {
                  Log.d(TAG, "Puzzle received: " + (dto != null));
                  if (dto != null) {
                    Log.d(TAG, "Board layout: " + dto.getPuzzle().getBoard().day);
                    Log.d(TAG, "Puzzle words count: " + dto.getPuzzle().getPuzzleWords().size());
                  }
                  userPuzzle.postValue(dto);
                  if (dto != null) {
                    currentPuzzle.postValue(dto.getPuzzle());
                    words.postValue(dto.getPuzzle().getPuzzleWords());
                    guesses.postValue(dto.getGuesses());
                  }
                },
                this::postThrowable
            )
    );
  }

  public void selectSquare(int position) {
    UserPuzzleDto.Puzzle puzzle = currentPuzzle.getValue();
    if (puzzle == null) {
      return;
    }

    int row = position / puzzle.getSize();
    int col = position % puzzle.getSize();
    Character cellChar = board.getValue()[row][col];

    if (cellChar == '0') return;

    boolean sameCellClicked = lastClickedRow != null && lastClickedCol != null &&
        lastClickedRow == row && lastClickedCol == col;

    UserPuzzleDto.Puzzle.PuzzleWord.Direction desiredDirection = sameCellClicked
        ? (lastDirection == UserPuzzleDto.Puzzle.PuzzleWord.Direction.ACROSS ? UserPuzzleDto.Puzzle.PuzzleWord.Direction.DOWN : UserPuzzleDto.Puzzle.PuzzleWord.Direction.ACROSS)
        : UserPuzzleDto.Puzzle.PuzzleWord.Direction.ACROSS;

    UserPuzzleDto.Puzzle.PuzzleWord matchedWord = null;
    for (UserPuzzleDto.Puzzle.PuzzleWord word : puzzle.getPuzzleWords()) {
      if (word.getDirection() == desiredDirection) {
        int startRow = word.getWordPosition().getRow();
        int startCol = word.getWordPosition().getColumn();
        int length = word.getWordPosition().getLength();

        if (word.getDirection() == UserPuzzleDto.Puzzle.PuzzleWord.Direction.ACROSS) {
          if (row == startRow && col >= startCol && col < startCol + length) {
            matchedWord = word;
            break;
          }
        } else {
          if (col == startCol && row >= startRow && row < startRow + length) {
            matchedWord = word;
            break;
          }
        }
      }
    }

    if (matchedWord != null) {
      selectedWord.postValue(matchedWord);
      selectedDirection.postValue(matchedWord.getDirection());
      lastClickedRow = row;
      lastClickedCol = col;
      lastDirection = matchedWord.getDirection();

      List<Integer> matchedPositions = new ArrayList<>();
      int startRow = matchedWord.getWordPosition().getRow();
      int startCol = matchedWord.getWordPosition().getColumn();
      int length = matchedWord.getWordPosition().getLength();

      if (matchedWord.getDirection() == UserPuzzleDto.Puzzle.PuzzleWord.Direction.ACROSS) {
        for (int offset = 0; offset < length; offset++) {
          matchedPositions.add(startRow * puzzle.getSize() + (startCol + offset));
        }
      } else {
        for (int offset = 0; offset < length; offset++) {
          matchedPositions.add((startRow + offset) * puzzle.getSize() + startCol);
        }
      }
      selectedCellPositions.postValue(matchedPositions);
    }
  }

  public void sendGuess(GuessDto guess) {
    throwable.setValue(null);
    pending.add(
        crossfyreService.sendGuess(
                userPuzzle.getValue().getPuzzle().getDate(),
                guess
            )
            .subscribe(
                // Convert GuessDto list to UserPuzzleDto.Guess list
                guessDtos -> {
                  List<UserPuzzleDto.Guess> convertedGuesses = convertGuessDto(guessDtos);
                  guesses.postValue(convertedGuesses);
                  fetchUserPuzzle(); // Refresh puzzle state
                },
                this::postThrowable
            )
    );
  }

  // Helper method to convert GuessDto to UserPuzzleDto.Guess
  private List<UserPuzzleDto.Guess> convertGuessDto(List<GuessDto> guessDto) {
    List<UserPuzzleDto.Guess> converted = new ArrayList<>();
    for (GuessDto dto : guessDto) {
      UserPuzzleDto.Guess guess = new UserPuzzleDto.Guess();
      guess.setGuess(dto.getGuess());

      UserPuzzleDto.Guess.GuessPosition position = new UserPuzzleDto.Guess.GuessPosition();
      position.setRow(dto.getGuessPosition().getRow());
      position.setColumn(dto.getGuessPosition().getColumn());
      guess.setGuessPosition(position);

      converted.add(guess);
    }
    return converted;
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
  public LiveData<Character[][]> getBoard() { return board; }
  public LiveData<Map<Integer, Integer>> getWordStartMap() { return wordStartMap; }
  public LiveData<UserPuzzleDto.Puzzle.PuzzleWord> getSelectedWord() { return selectedWord; }
  public LiveData<List<Integer>> getSelectedCellPositions() { return selectedCellPositions; }
  public LiveData<UserPuzzleDto.Puzzle.PuzzleWord.Direction> getSelectedDirection() { return selectedDirection; }
  public LiveData<List<UserPuzzleDto.Guess>> getGuesses() { return guesses; }
  public LiveData<GuessDto> getSelectedSquare() { return selectedSquare; }
  public LiveData<Throwable> getThrowable() { return throwable; }
  public LiveData<UserPuzzleDto> getUserPuzzle() { return userPuzzle; }
  public LiveData<UserPuzzleDto.Puzzle> getCurrentPuzzle() { return currentPuzzle; }
}