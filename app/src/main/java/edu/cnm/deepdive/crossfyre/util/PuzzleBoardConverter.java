package edu.cnm.deepdive.crossfyre.util;

import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzle;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzle.Guess;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzle.Guess.Puzzle;
import java.util.Collections;
import java.util.List;

public class PuzzleBoardConverter {

  // New method that works with just Puzzle
  public static Character[][] convertToBoard(Puzzle puzzle) {
    return convertToBoard(puzzle, Collections.emptyList());
  }

  // Method that works with both Puzzle and guesses
  public static Character[][] convertToBoard(Puzzle puzzle, List<Guess> guesses) {
    Character[][] board = initializeBoard(puzzle);
    applyGuesses(board, guesses);
    return board;
  }

  private static Character[][] initializeBoard(Puzzle puzzle) {
    int size = puzzle.getSize();
    Character[][] board = new Character[size][size];
    String boardString = puzzle.getBoard().day;

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        int index = i * size + j;
        board[i][j] = (index < boardString.length())
            ? boardString.charAt(index)
            : '0';
      }
    }
    return board;
  }

  private static void applyGuesses(Character[][] board, List<Guess> guesses) {
    for (Guess guess : guesses) {
      Guess.GuessPosition position = guess.getGuessPosition();
      int row = position.getGuessRow();
      int col = position.getGuessColumn();

      if (isValidPosition(board, row, col)) {
        board[row][col] = guess.getGuessChar();
      }
    }
  }

  private static boolean isValidPosition(Character[][] board, int row, int col) {
    return row >= 0 && row < board.length && col >= 0 && col < board[0].length;
  }
}