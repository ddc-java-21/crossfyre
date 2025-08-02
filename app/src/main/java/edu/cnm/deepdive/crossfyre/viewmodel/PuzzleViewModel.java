package edu.cnm.deepdive.crossfyre.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import edu.cnm.deepdive.crossfyre.model.dto.PuzzleWord;
import edu.cnm.deepdive.crossfyre.model.dto.PuzzleWord.Direction;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzle;
import java.util.ArrayList;
import java.util.List;

public class PuzzleViewModel extends ViewModel {

  private final MutableLiveData<List<PuzzleWord>> words = new MutableLiveData<>(new ArrayList<>());
  private final MutableLiveData<PuzzleWord.Direction> selectedDirection;
  private final MutableLiveData<PuzzleWord> selectedWord = new MutableLiveData<>();
  private final MutableLiveData<UserPuzzle> userPuzzle = new MutableLiveData<>();

  public PuzzleViewModel(MutableLiveData<Direction> selectedDirection) {
    this.selectedDirection = selectedDirection;
  }

  public LiveData<List<PuzzleWord>> getWords() {
    return words;
  }

    public LiveData<PuzzleWord> getSelectedWord() {
    return selectedWord;
  }

  public void selectWord(PuzzleWord word) {
    selectedWord.setValue(word);
  }

  public MutableLiveData<Direction> getSelectedDirection() {
    return selectedDirection;
  }

  public MutableLiveData<UserPuzzle> getUserPuzzle() {
    return userPuzzle;
  }

  public void selectSquare(int position) {
    int row = position / ;
    int col  =  position % puzzleSize
    // TODO: 8/1/2025 Figure out which puzzleWord occupies; update LiveData accordingly
  }

  //Include similar logic for clicking on same square
//  public void toggleDirection() {
//    PuzzleWord.Direction current = selectedDirection.getValue();
//    selectedDirection.setValue(current == PuzzleWord.Direction.ACROSS
//        ? PuzzleWord.Direction.DOWN
//        : PuzzleWord.Direction.ACROSS);
//  }

  // TODO: 8/1/2025 methods to handle which puzzleWord to look at


}
