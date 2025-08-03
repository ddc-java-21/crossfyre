package edu.cnm.deepdive.crossfyre.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import edu.cnm.deepdive.crossfyre.model.dto.PuzzleWord;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzle.Guess.Puzzle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PuzzleViewModel extends ViewModel {

  private final MutableLiveData<Puzzle> currentPuzzle = new MutableLiveData<>();
  private final MutableLiveData<Puzzle.PuzzleWord> selectedWord = new MutableLiveData<>();
  private final MutableLiveData<List<Integer>> selectedCellPosition = new MutableLiveData<>();

  private Integer lastClickedRow = null;
  private Integer lastClickedCol = null;
  private Puzzle.PuzzleWord.Direction lastDirection = null;

  // LiveData for board (grid of characters)
  private final LiveData<Character[][]> board = Transformations.map(currentPuzzle, puzzle -> {
    if (puzzle == null) return null;
    int size = puzzle.getSize();
    Character[][] grid = new Character[size][size];
    String layout = puzzle.getBoard().day;
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
      Transformations.map(currentPuzzle, puzzle -> {
        Map<Integer, Integer> map = new HashMap<>();
        if (puzzle == null || puzzle.getPuzzleWords() == null) {
          return map;
        }
        int size = puzzle.getSize();
        int clueNumber = 1;
        for (Puzzle.PuzzleWord word : puzzle.getPuzzleWords()) {
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

  public LiveData<Map<Integer, Integer>> getWordStartMap() {
    return wordStartMap;
  }

  public LiveData<Puzzle.PuzzleWord> getSelectedWord() {
    return selectedWord;
  }

  public LiveData<List<Integer>> getSelectedCellPosition() {
    return selectedCellPosition;
  }

  public void setPuzzle(Puzzle puzzle) {
    currentPuzzle.setValue(puzzle);
  }

  public void selectSquare(int position) {
    Puzzle puzzle = currentPuzzle.getValue();
    if (puzzle == null) return;

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
}
