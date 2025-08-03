package edu.cnm.deepdive.crossfyre.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import edu.cnm.deepdive.crossfyre.model.dto.PuzzleWord;
import edu.cnm.deepdive.crossfyre.model.dto.PuzzleWord.Direction;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzle;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzle.Guess.Puzzle;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzleDto.Puzzle.Board;
import java.util.ArrayList;
import java.util.List;

public class PuzzleViewModel extends ViewModel {

  private final MutableLiveData<List<PuzzleWord>> words = new MutableLiveData<>(new ArrayList<>());
  private final MutableLiveData<Direction> selectedDirection = new MutableLiveData<>(Direction.ACROSS);
  private final MutableLiveData<Puzzle.PuzzleWord> selectedWord = new MutableLiveData<>();

  // TODO: 8/1/25 Define field to hold reference to current Puzzle
  private final MutableLiveData<UserPuzzle> currentPuzzle = new MutableLiveData<>();
  private final MutableLiveData<Character[][]> board = new MutableLiveData<>();

  // Position of user clicked field
  private final MutableLiveData<PuzzleWord> position = new MutableLiveData<>();

  private Character[][] toBoard(Board boardEnum) {
    String flat = boardEnum.day;
    int size = currentPuzzle.getValue().getSize();
    Character[][] board = new Character[size][size];
    for (int i = 0; i < flat.length(); i++) {
      board[i / size][i % size] = flat.charAt(i);
    }
    return board;
  }

  public LiveData<Character[][]> getBoard() {
    return board;
  }

  public void setBoardFromPuzzle(Board boardEnum) {
    board.setValue(toBoard(boardEnum));
  }

  LiveData<UserPuzzle> getCurrentPuzzle() {
    return currentPuzzle;
  }

  public LiveData<List<PuzzleWord>> getWords() {
    return words;
  }

  public LiveData<PuzzleWord.Direction> getSelectedDirection() {
    return selectedDirection;
  }

  // Will let you know the direction anyway
  public LiveData<Puzzle.PuzzleWord> getSelectedWord() {
    return selectedWord;
  }

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



}
