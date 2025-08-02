package edu.cnm.deepdive.crossfyre.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import edu.cnm.deepdive.crossfyre.model.dto.PuzzleWord;
import edu.cnm.deepdive.crossfyre.model.dto.PuzzleWord.Direction;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzle.Guess.Puzzle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class PuzzleViewModel extends ViewModel {

  private final MutableLiveData<List<PuzzleWord>> words = new MutableLiveData<>(new ArrayList<>());
  private final MutableLiveData<Direction> selectedDirection = new MutableLiveData<>(Direction.ACROSS);
  private final MutableLiveData<Puzzle.PuzzleWord> selectedWord = new MutableLiveData<>();
  private final MutableLiveData<Character[][]> board = new MutableLiveData<>();
  // TODO: 8/1/25 Define field to hold reference to current Puzzle
  private final MutableLiveData<Puzzle> currentPuzzle = new MutableLiveData<>();
  // Position of user clicked field
  private final MutableLiveData<PuzzleWord> position = new MutableLiveData<>();
  private final MutableLiveData<List<Integer>> selectedCellPosition = new MutableLiveData<>();
  private final MutableLiveData<Map<Integer, Integer>> wordStartMap = new MutableLiveData<>();


  private Integer lastClickedRow = null;
  private Integer lastClickedCol = null;
  private Puzzle.PuzzleWord.Direction lastDirection = null;


  public MutableLiveData<Direction> getSelectedDirection() {
    return selectedDirection;
  }

  public MutableLiveData<Puzzle> getCurrentPuzzle() {
    return currentPuzzle;
  }

  public MutableLiveData<PuzzleWord> getPosition() {
    return position;
  }

  public MutableLiveData<List<Integer>> getSelectedCellPosition() {
    return selectedCellPosition;
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
  public void selectSquare(int position) {
    Puzzle puzzle = currentPuzzle.getValue();

    int row = position / puzzle.getSize();
    int col = position % puzzle.getSize();

    Puzzle.PuzzleWord.Direction desiredDirection;

    boolean sameCellClicked = (
        lastClickedRow != null && lastClickedCol != null && lastClickedRow == row && lastClickedCol
            == col);

    if (sameCellClicked) {
      desiredDirection = (lastDirection == Puzzle.PuzzleWord.Direction.ACROSS)
          ? Puzzle.PuzzleWord.Direction.DOWN
          : Puzzle.PuzzleWord.Direction.ACROSS;
    } else {
      desiredDirection = Puzzle.PuzzleWord.Direction.ACROSS;
    }

    // TODO: 8/1/25 Figure out which puzzleword that the row and col above is and update the live
    //  data accordingly like the clue displayed etc..
    Puzzle.PuzzleWord matchedWord = null;
    for (Puzzle.PuzzleWord word : puzzle.getPuzzleWords()) {
      CurrentWordPosition currentWordPosition = getCurrentWordPosition(word);

      if (word.getDirection() == Puzzle.PuzzleWord.Direction.ACROSS) {
        if (row == currentWordPosition.wordRow() && col >= currentWordPosition.wordCol()
            && col < currentWordPosition.wordCol() + currentWordPosition.length()) {
          matchedWord = word;
          break;
        }
      } else if (word.getDirection() == Puzzle.PuzzleWord.Direction.DOWN) {
        if (col == currentWordPosition.wordCol() && row >= currentWordPosition.wordRow()
            && row < currentWordPosition.wordRow() + currentWordPosition.length()) {
          matchedWord = word;
          break;
        }
      }
    }

    if (matchedWord == null && !sameCellClicked) {
      Puzzle.PuzzleWord.Direction fallbackDirection = Puzzle.PuzzleWord.Direction.DOWN;
      for (Puzzle.PuzzleWord word : puzzle.getPuzzleWords()) {
        if (word.getDirection() != fallbackDirection) {
          continue;
        }
        CurrentWordPosition pos = getCurrentWordPosition(word);

        if (word.getDirection() == Puzzle.PuzzleWord.Direction.DOWN) {
          if (col == pos.wordCol() && row >= pos.wordRow() && row < pos.wordRow() + pos.length()) {
            matchedWord = word;
            break;
          }
        }
      }
    }


    // Update selection if found
    if (matchedWord != null) {
      selectedWord.setValue(matchedWord);
      lastClickedRow = row;
      lastClickedCol = col;
      lastDirection = matchedWord.getDirection();
    }
    // DONE: might want to save the position in a field so it can be invoked in logic when the user clicks the same position it can change orientation

    List<Integer> matchedPositions = new ArrayList<>();
    CurrentWordPosition pos = getCurrentWordPosition(matchedWord);
    int startRow =  pos.wordRow();
    int startCol = pos.wordCol();
    int length = pos.length();

    if (matchedWord.getDirection() == Puzzle.PuzzleWord.Direction.ACROSS) {
      for (int offset = 0; offset < length; offset++) {
        int offsetCol = startCol + offset; // move right from starting column
        int positionIndex = (startRow * puzzle.getSize()) + offsetCol;
        matchedPositions.add(positionIndex);
      }
    } else if (matchedWord.getDirection() == Puzzle.PuzzleWord.Direction.DOWN) {
      for (int offset = 0; offset < length; offset++) {
        int offsetRow = startRow + offset; // move down from starting offsetRow
        int positionIndex = (offsetRow * puzzle.getSize()) + startCol;
        matchedPositions.add(positionIndex);
      }
    }
    selectedCellPosition.setValue(matchedPositions);
  }

  private void updateWordStartMap(Puzzle puzzle) {
    Map<Integer, Integer> map = new HashMap<>();
    for (int i = 0; i < puzzle.getPuzzleWords().size(); i++) {
      Puzzle.PuzzleWord word = puzzle.getPuzzleWords().get(i);
      int startRow = word.getWordPosition().getRow();
      int startCol = word.getWordPosition().getColumn();
      int positionIndex = startRow * puzzle.getSize() + startCol;
      map.put(positionIndex, i + 1); // 1-based numbering
    }
    wordStartMap.setValue(map);
  }

  private static @NotNull CurrentWordPosition getCurrentWordPosition(Puzzle.PuzzleWord word) {
    int wordRow = word.getWordPosition().getRow();
    int wordCol = word.getWordPosition().getColumn();
    int length = word.getWordPosition().getLength();
    return new CurrentWordPosition(wordRow, wordCol, length);
  }

  public MutableLiveData<Character[][]> getBoard() {
    return board;
  }

  public void loadBoard(Character[][] newBoard) {
    board.setValue(newBoard);
  }

  public MutableLiveData<Map<Integer, Integer>> getWordStartMap() {
    return wordStartMap;
  }

  private record CurrentWordPosition(int wordRow, int wordCol, int length) {}

  // Call this when setting the current puzzle
  public void setCurrentPuzzle(Puzzle puzzle) {
    currentPuzzle.setValue(puzzle);
    updateWordStartMap(puzzle);
  }

//  public void toggleDirection() {
//    Direction current = selectedDirection.getValue();
//    selectedDirection.setValue(current == Direction.ACROSS
//        ? Direction.DOWN
//        : Direction.ACROSS);
//  }


  // Need methods that the UI controller can invoke when the user clicks so that it knows which
  // puzzleword the user is looking at by new word or orientation switch


}
