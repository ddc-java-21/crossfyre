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
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.inject.Inject;

/**
 * ViewModel responsible for handling puzzle data and user interactions for the crossword puzzle.
 * <p>
 * Manages the current user profile, current puzzle, puzzle state including guesses, selected word,
 * and selected cells, as well as interaction with the backend service to fetch and update puzzle data.
 */
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

  /**
   * Constructs a PuzzleViewModel with the provided CrossfyreService.
   *
   * @param crossfyreService the service for network operations related to the puzzle and user.
   */
  @Inject
  public PuzzleViewModel(CrossfyreService crossfyreService) {
    this.crossfyreService = crossfyreService;
    userPuzzle = new MutableLiveData<>();
    currentPuzzle = Transformations.map(userPuzzle, (up) -> (up != null) ? up.getPuzzle() : null);
    wordStarts = Transformations.map(userPuzzle, (up) -> (up != null) ? up.getPuzzle().getWordStarts() : null);
    words = Transformations.map(currentPuzzle, (cp) -> (cp != null) ? cp.getPuzzleWords() : null);
    selectedWord = new MutableLiveData<>();
    selectedCellPositions = new MutableLiveData<>();
    selectedDirection = Transformations.map(selectedWord, (sw) -> (sw != null) ? sw.getDirection() : null);
    guesses = Transformations.map(userPuzzle, (up) -> (up != null) ? up.getGuesses() : null);
    selectedSquare = new MutableLiveData<>();
    pending = new CompositeDisposable();
    throwable = new MutableLiveData<>();
    grid = Transformations.map(currentPuzzle, Puzzle::getGrid);
    fetchCurrentUser();
    fetchUserPuzzle();
  }

  /**
   * Fetches the current logged-in user profile from the backend service.
   * Updates the LiveData currentUser or throwable on failure.
   */
  private void fetchCurrentUser() {
    throwable.setValue(null);
    crossfyreService.getMyProfile()
        .subscribe(
            currentUser::postValue,
            this::postThrowable,
            pending
        );
  }

  /**
   * Fetches the puzzle for the current date from the backend service.
   * Updates the LiveData userPuzzle or throwable on failure.
   */
  private void fetchUserPuzzle() {
    throwable.setValue(null);
    crossfyreService.getUserPuzzle(LocalDate.now())
        .subscribe(
            this::handleUserPuzzle,
            this::postThrowable,
            pending
        );
  }

  /**
   * Handles a fetched UserPuzzleDto by posting it to the userPuzzle LiveData.
   *
   * @param dto the puzzle data transfer object fetched from the backend.
   */
  private void handleUserPuzzle(UserPuzzleDto dto) {
    if (dto != null) {
      userPuzzle.postValue(dto);
    }
  }

  /**
   * Selects a square in the puzzle grid by position index.
   * Updates the selected word and highlighted cell positions accordingly.
   *
   * @param position the linear position in the puzzle grid.
   */
  public void selectSquare(int position) {
    UserPuzzleDto.Puzzle puzzle = userPuzzle.getValue().getPuzzle();
    int row = position / puzzle.getSize();
    int col = position % puzzle.getSize();
    boolean sameCellClicked = lastClickedRow == row && lastClickedCol == col;

    List<PuzzleWord> candidates = puzzle.getPuzzleWords()
        .stream()
        .filter(word -> word.includes(row, col))
        .collect(Collectors.toList());

    if (candidates.isEmpty()) {
      return;
    }

    PuzzleWord matchedWord = (candidates.size() == 1)
        ? candidates.get(0)
        : candidates.stream()
            .filter(word -> sameCellClicked
                ? !word.equals(selectedWord.getValue())
                : word.getDirection() == Direction.ACROSS)
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

    List<Integer> selectedPositions = IntStream.range(0, matchedWord.getWordPosition().getLength())
        .map(sp -> {
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

  /**
   * Sends a guess for a letter in the puzzle to the backend service.
   *
   * @param guess the guess object containing the guessed letter and position.
   */
  public void sendGuess(Guess guess) {
    throwable.setValue(null);
    crossfyreService.sendGuess(userPuzzle.getValue().getPuzzle().getDate(), guess)
        .subscribe(
            userPuzzle::postValue,
            this::postThrowable,
            pending
        );
  }

  /**
   * Selects a puzzle word programmatically.
   *
   * @param word the PuzzleWord to select.
   */
  public void selectWord(PuzzleWord word) {
    selectedWord.setValue(word);
  }

  /**
   * Clears pending disposables when lifecycle owner stops.
   *
   * @param owner the lifecycle owner.
   */
  @Override
  public void onStop(@NonNull LifecycleOwner owner) {
    pending.clear();
    DefaultLifecycleObserver.super.onStop(owner);
  }

  /**
   * Posts a Throwable error to the throwable LiveData and logs the error.
   *
   * @param throwable the Throwable to post.
   */
  private void postThrowable(Throwable throwable) {
    Log.e(TAG, throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

  /**
   * Gets the puzzle grid as a LiveData of a 2D boolean array.
   *
   * @return the grid LiveData.
   */
  public LiveData<boolean[][]> getGrid() {
    return grid;
  }

  /**
   * Gets the array of word start positions as LiveData.
   *
   * @return the word starts LiveData.
   */
  public LiveData<int[]> getWordStarts() {
    return wordStarts;
  }

  /**
   * Gets the currently selected puzzle word as LiveData.
   *
   * @return the selected word LiveData.
   */
  public LiveData<UserPuzzleDto.Puzzle.PuzzleWord> getSelectedWord() {
    return selectedWord;
  }

  /**
   * Gets the list of selected cell positions as LiveData.
   *
   * @return the selected cell positions LiveData.
   */
  public LiveData<List<Integer>> getSelectedCellPositions() {
    return selectedCellPositions;
  }

  /**
   * Gets the currently selected direction of the puzzle word as LiveData.
   *
   * @return the selected direction LiveData.
   */
  public LiveData<UserPuzzleDto.Puzzle.PuzzleWord.Direction> getSelectedDirection() {
    return selectedDirection;
  }

  /**
   * Gets the list of guesses made by the user as LiveData.
   *
   * @return the guesses LiveData.
   */
  public LiveData<List<UserPuzzleDto.Guess>> getGuesses() {
    return guesses;
  }

  /**
   * Gets the currently selected square position as LiveData.
   *
   * @return the selected square LiveData.
   */
  public LiveData<Integer> getSelectedSquare() {
    return selectedSquare;
  }

  /**
   * Gets any errors encountered during backend operations as LiveData.
   *
   * @return the throwable LiveData.
   */
  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  /**
   * Gets the current UserPuzzleDto as LiveData.
   *
   * @return the user puzzle LiveData.
   */
  public LiveData<UserPuzzleDto> getUserPuzzle() {
    return userPuzzle;
  }

  /**
   * Gets the current Puzzle object as LiveData.
   *
   * @return the current puzzle LiveData.
   */
  public LiveData<UserPuzzleDto.Puzzle> getCurrentPuzzle() {
    return currentPuzzle;
  }

}
