package edu.cnm.deepdive.crossfyre.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import edu.cnm.deepdive.crossfyre.model.dto.PuzzleWord;
import edu.cnm.deepdive.crossfyre.model.dto.PuzzleWord.Direction;
import java.util.ArrayList;
import java.util.List;

public class PuzzleViewModel extends ViewModel {

  private final MutableLiveData<List<PuzzleWord>> words = new MutableLiveData<>(new ArrayList<>());
  private final MutableLiveData<PuzzleWord.Direction> selectedDirection = new MutableLiveData<>(PuzzleWord.Direction.ACROSS);
  private final MutableLiveData<PuzzleWord> selectedWord = new MutableLiveData<>();
  // TODO: 8/1/25 Define field to hold reference to current Puzzle

  public LiveData<List<PuzzleWord>> getWords() {
    return words;
  }

//  public LiveData<PuzzleWord.Direction> getSelectedDirection() {
//    return selectedDirection;
//  }

  // Will let you know the direction anyway
  public LiveData<PuzzleWord> getSelectedWord() {
    return selectedWord;
  }

  //UI logic will never set the words
//  public void setWords(List<PuzzleWord> newWords) {
//    words.setValue(newWords);
//  }

//  public void selectWord(PuzzleWord word) {
//    selectedWord.setValue(word);
//  }

  //Should have entire puzzle object because list of words doesn't tell you puzzle size which we need here
  public void selectSquare(int position){
    // row = position / puzzle.size
    // col = position % puzzle.size
    // TODO: 8/1/25 Figure out which puzzleword that the row and col above is and update the live
    //  data accordingly like the clue displayed etc..

    // might want to save the position in a field so it can be invoked in logic when the user clicks the same position it can change orientation
  }

  public void toggleDirection() {
    PuzzleWord.Direction current = selectedDirection.getValue();
    selectedDirection.setValue(current == PuzzleWord.Direction.ACROSS
        ? PuzzleWord.Direction.DOWN
        : PuzzleWord.Direction.ACROSS);
  }

  // Need methods that the UI controller can invoke when the user clicks so that it knows which
  // puzzleword the user is looking at by new word or orientation switch



}
