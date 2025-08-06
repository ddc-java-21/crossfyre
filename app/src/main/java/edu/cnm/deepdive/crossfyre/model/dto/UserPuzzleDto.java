package edu.cnm.deepdive.crossfyre.model.dto;

import androidx.annotation.Nullable;
import com.google.gson.annotations.Expose;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) representing a user's interaction with a puzzle,
 * including solve status, guesses, and puzzle structure.
 */
public class UserPuzzleDto {

  @Expose
  private UUID key;

  @Expose
  private Instant created;

  @Expose
  private Boolean isSolved;

  @Expose
  private List<Guess> guesses;

  @Expose
  private Puzzle puzzle;

  /**
   * Returns the unique identifier for this user puzzle instance.
   *
   * @return UUID of the user puzzle.
   */
  public UUID getKey() {
    return key;
  }

  /**
   * Sets the unique identifier for this user puzzle instance.
   *
   * @param key UUID to assign.
   */
  public void setKey(UUID key) {
    this.key = key;
  }

  /**
   * Returns the timestamp when the puzzle instance was created.
   *
   * @return creation timestamp.
   */
  public Instant getCreated() {
    return created;
  }

  /**
   * Sets the timestamp when the puzzle instance was created.
   *
   * @param created creation timestamp.
   */
  public void setCreated(Instant created) {
    this.created = created;
  }

  /**
   * Returns whether the puzzle is marked as solved.
   *
   * @return {@code true} if solved, otherwise {@code false}.
   */
  public Boolean getSolved() {
    return isSolved;
  }

  /**
   * Sets whether the puzzle is marked as solved.
   *
   * @param solved {@code true} if solved.
   */
  public void setSolved(Boolean solved) {
    isSolved = solved;
  }

  /**
   * Returns the list of guesses made by the user.
   *
   * @return list of {@link Guess} objects.
   */
  public List<Guess> getGuesses() {
    return guesses;
  }

  /**
   * Sets the list of guesses made by the user.
   *
   * @param guesses list of {@link Guess} objects.
   */
  public void setGuesses(List<Guess> guesses) {
    this.guesses = guesses;
  }

  /**
   * Returns the puzzle associated with this user puzzle instance.
   *
   * @return {@link Puzzle} object.
   */
  public Puzzle getPuzzle() {
    return puzzle;
  }

  /**
   * Sets the puzzle associated with this user puzzle instance.
   *
   * @param puzzle {@link Puzzle} object.
   */
  public void setPuzzle(Puzzle puzzle) {
    this.puzzle = puzzle;
  }

  /**
   * Represents the puzzle structure and metadata, including dimensions, words, and date.
   */
  public static class Puzzle {

    @Expose
    private UUID key;

    @Expose
    private Integer size;

    @Expose
    private Instant created;

    @Expose
    private LocalDate date;

    @Expose
    private List<PuzzleWord> puzzleWords;

    /**
     * Returns the unique identifier of the puzzle.
     *
     * @return puzzle UUID.
     */
    public UUID getKey() {
      return key;
    }

    /**
     * Sets the unique identifier of the puzzle.
     *
     * @param key UUID to assign.
     */
    public void setKey(UUID key) {
      this.key = key;
    }

    /**
     * Returns the dimension (width and height) of the puzzle grid.
     *
     * @return puzzle size.
     */
    public Integer getSize() {
      return size;
    }

    /**
     * Sets the size (dimension) of the puzzle grid.
     *
     * @param size size to assign.
     */
    public void setSize(Integer size) {
      this.size = size;
    }

    /**
     * Returns the creation timestamp of the puzzle.
     *
     * @return creation time.
     */
    public Instant getCreated() {
      return created;
    }

    /**
     * Sets the creation timestamp of the puzzle.
     *
     * @param created time to assign.
     */
    public void setCreated(Instant created) {
      this.created = created;
    }

    /**
     * Returns the date the puzzle is associated with.
     *
     * @return puzzle date.
     */
    public LocalDate getDate() {
      return date;
    }

    /**
     * Sets the date the puzzle is associated with.
     *
     * @param date date to assign.
     */
    public void setDate(LocalDate date) {
      this.date = date;
    }

    /**
     * Returns the list of puzzle words in the puzzle.
     *
     * @return list of {@link PuzzleWord} objects.
     */
    public List<PuzzleWord> getPuzzleWords() {
      return puzzleWords;
    }

    /**
     * Sets the list of puzzle words in the puzzle.
     *
     * @param puzzleWords list of {@link PuzzleWord} objects.
     */
    public void setPuzzleWords(List<PuzzleWord> puzzleWords) {
      this.puzzleWords = puzzleWords;
    }

    /**
     * Returns an array of start positions (flattened row-major) of all puzzle words.
     *
     * @return array of start indices.
     */
    public int[] getWordStarts() {
      return puzzleWords
          .stream()
          .mapToInt(
              (word) -> word.getWordPosition().getRow() * size + word.getWordPosition().getColumn())
          .sorted()
          .distinct()
          .toArray();
    }

    /**
     * Returns a 2D boolean grid representing the positions of all letters in puzzle words.
     *
     * @return grid of letter positions.
     */
    public boolean[][] getGrid() {
      boolean[][] grid = new boolean[size][size];
      puzzleWords.forEach((word) -> {
        for (
            int row = word.getWordPosition().getRow(), col = word.getWordPosition().getColumn(), pos = 0;
            pos < word.getWordPosition().getLength();
            pos++, row += word.getDirection().rowOffset(), col += word.getDirection().columnOffset()
        ) {
          grid[row][col] = true;
        }
      });
      return grid;
    }

    /**
     * Represents a word in the puzzle with its clue, direction, and position.
     */
    public static class PuzzleWord {

      @Expose
      private String clue;

      @Expose
      private Direction direction;

      @Expose
      private WordPosition wordPosition;

      /**
       * Returns the clue for this word.
       *
       * @return the clue string.
       */
      public String getClue() {
        return clue;
      }

      /**
       * Sets the clue for this word.
       *
       * @param clue clue string.
       */
      public void setClue(String clue) {
        this.clue = clue;
      }

      /**
       * Returns the direction of this word.
       *
       * @return {@link Direction} of the word.
       */
      public Direction getDirection() {
        return direction;
      }

      /**
       * Sets the direction of this word.
       *
       * @param direction direction to assign.
       */
      public void setDirection(Direction direction) {
        this.direction = direction;
      }

      /**
       * Returns the word's starting position and length.
       *
       * @return {@link WordPosition} object.
       */
      public WordPosition getWordPosition() {
        return wordPosition;
      }

      /**
       * Sets the word's starting position and length.
       *
       * @param wordPosition {@link WordPosition} object.
       */
      public void setWordPosition(WordPosition wordPosition) {
        this.wordPosition = wordPosition;
      }

      /**
       * Determines whether a given cell (row, column) is included in this word's path.
       *
       * @param row row index.
       * @param column column index.
       * @return {@code true} if the cell is part of the word.
       */
      public boolean includes(int row, int column) {
        return row >= wordPosition.getRow()
            && row < wordPosition.getRow() + Math.max(direction.rowOffset() * wordPosition.getLength(), 1)
            && column >= wordPosition.getColumn()
            && column < wordPosition.getColumn() + Math.max(direction.columnOffset() * wordPosition.getLength(), 1);
      }

      @Override
      public int hashCode() {
        return Objects.hash(wordPosition, direction);
      }

      @Override
      public boolean equals(@Nullable Object obj) {
        if (obj == this) return true;
        if (obj instanceof PuzzleWord other) {
          return direction == other.direction && wordPosition.equals(other.wordPosition);
        }
        return false;
      }

      /**
       * Represents the starting position and length of a puzzle word.
       */
      public static class WordPosition {

        @Expose
        private int row;

        @Expose
        private int column;

        @Expose
        private int length;

        /**
         * Returns the row index of the word's start.
         *
         * @return row index.
         */
        public int getRow() {
          return row;
        }

        /**
         * Sets the row index of the word's start.
         *
         * @param row row index.
         */
        public void setRow(int row) {
          this.row = row;
        }

        /**
         * Returns the column index of the word's start.
         *
         * @return column index.
         */
        public int getColumn() {
          return column;
        }

        /**
         * Sets the column index of the word's start.
         *
         * @param column column index.
         */
        public void setColumn(int column) {
          this.column = column;
        }

        /**
         * Returns the length of the word.
         *
         * @return word length.
         */
        public int getLength() {
          return length;
        }

        /**
         * Sets the length of the word.
         *
         * @param length word length.
         */
        public void setLength(int length) {
          this.length = length;
        }

        @Override
        public int hashCode() {
          return Objects.hash(row, column, length);
        }

        @Override
        public boolean equals(@Nullable Object obj) {
          if (obj == this) return true;
          if (obj instanceof WordPosition other) {
            return row == other.row && column == other.column && length == other.length;
          }
          return false;
        }
      }

      /**
       * Enum representing word directions on the puzzle grid.
       */
      public enum Direction {
        ACROSS(0, 1),
        DOWN(1, 0);

        private final int rowOffset;
        private final int columnOffset;

        Direction(int rowOffset, int columnOffset) {
          this.rowOffset = rowOffset;
          this.columnOffset = columnOffset;
        }

        /**
         * Returns the row increment per character.
         *
         * @return row offset.
         */
        public int rowOffset() {
          return rowOffset;
        }

        /**
         * Returns the column increment per character.
         *
         * @return column offset.
         */
        public int columnOffset() {
          return columnOffset;
        }
      }
    }
  }

  /**
   * Represents a single user guess on the puzzle board.
   */
  public static class Guess {

    @Expose
    private Character guess;

    @Expose
    private GuessPosition guessPosition;

    /**
     * Returns the guessed character.
     *
     * @return guessed character.
     */
    public Character getGuess() {
      return guess;
    }

    /**
     * Sets the guessed character.
     *
     * @param guess guessed character.
     */
    public void setGuess(Character guess) {
      this.guess = guess;
    }

    /**
     * Returns the position of the guess on the board.
     *
     * @return {@link GuessPosition} object.
     */
    public GuessPosition getGuessPosition() {
      return guessPosition;
    }

    /**
     * Sets the position of the guess on the board.
     *
     * @param guessPosition {@link GuessPosition} object.
     */
    public void setGuessPosition(GuessPosition guessPosition) {
      this.guessPosition = guessPosition;
    }

    /**
     * Represents the (row, column) position of a guess.
     */
    public static class GuessPosition {

      @Expose
      private int row;

      @Expose
      private int column;

      /**
       * Returns the row index of the guess.
       *
       * @return row index.
       */
      public int getRow() {
        return row;
      }

      /**
       * Sets the row index of the guess.
       *
       * @param row row index.
       */
      public void setRow(int row) {
        this.row = row;
      }

      /**
       * Returns the column index of the guess.
       *
       * @return column index.
       */
      public int getColumn() {
        return column;
      }

      /**
       * Sets the column index of the guess.
       *
       * @param column column index.
       */
      public void setColumn(int column) {
        this.column = column;
      }
    }
  }
}
