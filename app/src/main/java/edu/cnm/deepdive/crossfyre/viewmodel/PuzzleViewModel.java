package edu.cnm.deepdive.crossfyre.viewmodel;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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
import java.util.List;
import javax.inject.Inject;

@HiltViewModel
public class PuzzleViewModel extends ViewModel implements DefaultLifecycleObserver {

  private static final String TAG = PuzzleViewModel.class.getSimpleName();
  private final CrossfyreService crossfyreService;
  private final MutableLiveData<User> currentUser;

  private final MutableLiveData<List<PuzzleWord>> words = new MutableLiveData<>(new ArrayList<>());
  private final MutableLiveData<Direction> selectedDirection = new MutableLiveData<>(Direction.ACROSS);
  private final MutableLiveData<Puzzle.PuzzleWord> selectedWord = new MutableLiveData<>();


  private final MutableLiveData<List<GuessDto>> guesses;
  private final MutableLiveData<UserPuzzleDto> currentUserPuzzle;
  private final MutableLiveData<GuessDto> selectedSquare;
  private final MutableLiveData<Boolean> solved;

  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  private Disposable description;


  // TODO: 8/1/25 Define field to hold reference to current Puzzle
  private final MutableLiveData<Puzzle> currentPuzzle = new MutableLiveData<>();

  // Position of user clicked field
  private final MutableLiveData<PuzzleWord> position = new MutableLiveData<>();



  public MutableLiveData<List<GuessDto>> getGuesses() {
    return guesses;
  }

  public MutableLiveData<UserPuzzleDto> getCurrentUserPuzzle() {
    return currentUserPuzzle;
  }

  public MutableLiveData<GuessDto> getSelectedSquare() {
    return selectedSquare;
  }

  @Inject
  public PuzzleViewModel(CrossfyreService crossfyreService) {
    this.crossfyreService = crossfyreService;
    currentUser = new MutableLiveData<>();
    guesses = new MutableLiveData<>(new ArrayList<>());
    currentUserPuzzle = new MutableLiveData<>();
    selectedSquare = new MutableLiveData<>();
    solved = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    fetchCurrentUser();
    fetchCurrentUserPuzzle();
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

  public LiveData<List<PuzzleWord>> getWords() {
    return words;
  }

//  public LiveData<PuzzleWord.Direction> getSelectedDirection() {
//    return selectedDirection;
//  }

  // Will let you know the direction anyway
  public LiveData<Puzzle.PuzzleWord> getSelectedWord() {
    return selectedWord;
  }
  // UI logic will never set the words

//  public void setWords(List<PuzzleWord> newWords) {
//    words.setValue(newWords);
//  }

//  public void selectWord(PuzzleWord word) {
//    selectedWord.setValue(word);
//  }

  // Should have entire puzzle object because list of words doesn't tell you puzzle size which we need here
  public void selectSquare(int position){
    Puzzle puzzle = new Puzzle();

    int row = position / puzzle.getSize();
     int col = position % puzzle.getSize();

    // TODO: 8/1/25 Figure out which puzzleword that the row and col above is and update the live
    //  data accordingly like the clue displayed etc..
    Puzzle.PuzzleWord matchedWord = null;
    for(Puzzle.PuzzleWord word : puzzle.getPuzzleWords()) {
      int wordRow = word.getWordPosition().getRow();
      int wordCol = word.getWordPosition().getColumn();
      int length = word.getWordPosition().getLength();

      if(word.getDirection() == Puzzle.PuzzleWord.Direction.ACROSS) {
        if(row == wordRow && col >= wordCol && col < wordCol + length) {
          matchedWord = word;
          break;
        }
      } else if (word.getDirection() == Puzzle.PuzzleWord.Direction.DOWN) {
        if (col == wordCol && row >= wordRow && row < wordRow + length) {
          matchedWord = word;
          break;
        }
      }
    }
    if(matchedWord != null) {
      selectedWord.setValue(matchedWord);
    }
    // might want to save the position in a field so it can be invoked in logic when the user clicks the same position it can change orientation
  }

  public void toggleDirection() {
    Direction current = selectedDirection.getValue();
    selectedDirection.setValue(current == Direction.ACROSS
        ? Direction.DOWN
        : Direction.ACROSS);
  }

  // Need methods that the UI controller can invoke when the user clicks so that it knows which
  // puzzleword the user is looking at by new word or orientation switch


  private void fetchCurrentUserPuzzle() {
    throwable.setValue(null);
    crossfyreService
        .getUserPuzzle(Instant.now().truncatedTo(ChronoUnit.DAYS))
        .subscribe(
            value -> {
              currentUserPuzzle.postValue(value);
            },
            this::postThrowable,
            pending
        );
  }

  public void sendGuess(GuessDto guess) {
    throwable.setValue(null);
    //noinspection DataFlowIssue
    crossfyreService
        .sendGuess(currentUserPuzzle.getValue().getPuzzle().getDate(), selectedSquare.getValue())
        .subscribe(
            currentUserPuzzle::postValue,
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



}
