package edu.cnm.deepdive.crossfyre.model.dto;

import androidx.annotation.Nullable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class UserPuzzleDto {

  @Expose
  private UUID key;
  @Expose
  private Instant created;
  @Expose
  @SerializedName("is_solved")
  private Boolean isSolved;
  @Expose
  private List<Guess> guesses;
  @Expose
  private Puzzle puzzle;


  public UUID getKey() {
    return key;
  }

  public void setKey(UUID key) {
    this.key = key;
  }

  public Instant getCreated() {
    return created;
  }

  public void setCreated(Instant created) {
    this.created = created;
  }

  public Boolean getSolved() {
    return isSolved;
  }

  public void setSolved(Boolean solved) {
    isSolved = solved;
  }

  public List<Guess> getGuesses() {
    return guesses;
  }

  public void setGuesses(List<Guess> guesses) {
    this.guesses = guesses;
  }

  public Puzzle getPuzzle() {
    return puzzle;
  }

  public void setPuzzle(Puzzle puzzle) {
    this.puzzle = puzzle;
  }


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

    public UUID getKey() {
      return key;
    }

    public void setKey(UUID key) {
      this.key = key;
    }

    public Integer getSize() {
      return size;
    }

    public void setSize(Integer size) {
      this.size = size;
    }

    public Instant getCreated() {
      return created;
    }

    public void setCreated(Instant created) {
      this.created = created;
    }

    public LocalDate getDate() {
      return date;
    }

    public void setDate(LocalDate date) {
      this.date = date;
    }

    public List<PuzzleWord> getPuzzleWords() {
      return puzzleWords;
    }

    public void setPuzzleWords(
        List<PuzzleWord> puzzleWords) {
      this.puzzleWords = puzzleWords;
    }

    public int[] getWordStarts() {
      return puzzleWords
          .stream()
          .mapToInt(
              (word) -> word.getWordPosition().getRow() * size + word.getWordPosition().getColumn())
          .sorted()
          .distinct()
          .toArray();
    }

    public boolean[][] getGrid() {
      boolean[][] grid = new boolean[size][size];
      puzzleWords.forEach((word) -> {
        for (
            int row = word.getWordPosition().getRow(), col = word.getWordPosition()
                .getColumn(), position = 0;
            position < word.getWordPosition().getLength();
            position++, row += word.getDirection().rowOffset(), col += word.getDirection()
                .columnOffset()
        ) {
          grid[row][col] = true;
        }
      });
      return grid;
    }

    public static class PuzzleWord {

      @Expose
      private String clue;
      @Expose
      private Direction direction;


      @Expose
      private WordPosition wordPosition;


      public String getClue() {
        return clue;
      }

      public void setClue(String clue) {
        this.clue = clue;
      }

      public Direction getDirection() {
        return direction;
      }

      public void setDirection(
          Direction direction) {
        this.direction = direction;
      }

      public WordPosition getWordPosition() {
        return wordPosition;
      }

      public void setWordPosition(
          WordPosition wordPosition) {
        this.wordPosition = wordPosition;
      }

      public boolean includes(int row, int column) {
        return row >= wordPosition.getRow()
            && row < wordPosition.getRow() + Math.max(
            direction.rowOffset() * wordPosition.getLength(), 1)
            && column >= wordPosition.getColumn()
            && column < wordPosition.getColumn() + Math.max(
            direction.columnOffset() * wordPosition.getLength(), 1);
      }


      @Override
      public int hashCode() {
        return Objects.hash(wordPosition, direction);
      }

      @Override
      public boolean equals(@Nullable Object obj) {
        boolean comparison;
        if (obj == this) {
          comparison = true;
        } else if (obj instanceof PuzzleWord other) {
          comparison =
              (this.direction == other.direction) && (this.wordPosition.equals(other.wordPosition));
        } else {
          comparison = false;
        }
        return comparison;
      }

      public static class WordPosition {

        @Expose
        private int row;
        @Expose
        private int column;
        @Expose
        private int length;


        public int getRow() {
          return row;
        }

        public void setRow(int row) {
          this.row = row;
        }

        public int getColumn() {
          return column;
        }

        public void setColumn(int column) {
          this.column = column;
        }

        public int getLength() {
          return length;
        }

        public void setLength(int length) {
          this.length = length;
        }

        @Override
        public int hashCode() {
          return Objects.hash(row, column, length);
        }

        @Override
        public boolean equals(@Nullable Object obj) {
          boolean comparison;
          if (obj == this) {
            comparison = true;
          } else if (obj instanceof WordPosition other) {
            comparison = (this.row == other.row) && (this.column == other.column) && (this.length
                == other.length);
          } else {
            comparison = false;
          }
          return comparison;
        }
      }

      public enum Direction {
        ACROSS(0, 1),
        DOWN(1, 0);

        private final int rowOffset;
        private final int columnOffset;

        Direction(int rowOffset, int columnOffset) {
          this.rowOffset = rowOffset;
          this.columnOffset = columnOffset;
        }

        public int rowOffset() {
          return rowOffset;
        }

        public int columnOffset() {
          return columnOffset;
        }
      }


    }

  }

  public static class Guess {

    @Expose
    private Character guess;
    @Expose
    private GuessPosition guessPosition;


    public Character getGuess() {
      return guess;
    }

    public void setGuess(Character guess) {
      this.guess = guess;
    }

    public GuessPosition getGuessPosition() {
      return guessPosition;
    }

    public void setGuessPosition(
        GuessPosition guessPosition) {
      this.guessPosition = guessPosition;
    }

    public static class GuessPosition {

      @Expose
      private int row;
      @Expose
      private int column;


      public int getRow() {
        return row;
      }

      public void setRow(int row) {
        this.row = row;
      }

      public int getColumn() {
        return column;
      }

      public void setColumn(int column) {
        this.column = column;
      }
    }

  }


}
