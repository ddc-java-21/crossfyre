package edu.cnm.deepdive.crossfyre.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import edu.cnm.deepdive.crossfyre.model.dto.PuzzleWord;
import java.util.ArrayList;
import java.util.List;

public class PuzzleViewModel {

  private final MutableLiveData<List<PuzzleWord>> words = new MutableLiveData<>(new ArrayList<>());
  private final MutableLiveData<PuzzleWord.Direction> selectedDirection = new MutableLiveData<>(PuzzleWord.Direction.ACROSS);
  private final MutableLiveData<PuzzleWord> selectedWord = new MutableLiveData<>();

  public LiveData<List<PuzzleWord>> getWords() {
    return words;
  }

  public LiveData<PuzzleWord.Direction> getSelectedDirection() {
    return selectedDirection;
  }

  public LiveData<PuzzleWord> getSelectedWord() {
    return selectedWord;
  }

  public void setWords(List<PuzzleWord> newWords) {
    words.setValue(newWords);
  }

  public void selectWord(PuzzleWord word) {
    selectedWord.setValue(word);
  }

  public void toggleDirection() {
    PuzzleWord.Direction current = selectedDirection.getValue();
    selectedDirection.setValue(current == PuzzleWord.Direction.ACROSS
        ? PuzzleWord.Direction.DOWN
        : PuzzleWord.Direction.ACROSS);
  }


}
