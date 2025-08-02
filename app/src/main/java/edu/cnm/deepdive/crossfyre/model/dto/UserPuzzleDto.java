package edu.cnm.deepdive.crossfyre.model.dto;

import com.google.gson.annotations.Expose;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class UserPuzzleDto {

  @Expose private UUID key;
  @Expose private Instant created;
  @Expose private Boolean isSolved;
  @Expose private List<Guess> guesses;


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

  public static class Guess {

    @Expose private Character guess;
    @Expose private GuessPosition guessPosition;


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

      @Expose private int row;
      @Expose private int column;


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

  @Expose private Puzzle puzzle;


  public Puzzle getPuzzle() {
    return puzzle;
  }

  public void setPuzzle(Puzzle puzzle) {
    this.puzzle = puzzle;
  }

  public static class Puzzle {

    @Expose private UUID key;
    @Expose private Integer size;
    @Expose private Board board;

    public enum Board {
      SUNDAY("0__________________0____0"),
      MONDAY("0___00___0_______________"),
      TUESDAY("00_____________________00"),
      WEDNESDAY("___00_______________00___"),
      THURSDAY("0___0_______________0___0"),
      FRIDAY("____0____0_____0____0____"),
      SATURDAY("___00____0_____0____00___");

      @Expose
      public final String day;

      Board(String day) {
        this.day = day;
      }

    }

    @Expose private Instant created;
    @Expose private Instant date;
    @Expose private List<PuzzleWord> puzzleWords;


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

    public Board getBoard() {
      return board;
    }

    public void setBoard(Board board) {
      this.board = board;
    }

    public Instant getCreated() {
      return created;
    }

    public void setCreated(Instant created) {
      this.created = created;
    }

    public Instant getDate() {
      return date;
    }

    public void setDate(Instant date) {
      this.date = date;
    }

    public List<PuzzleWord> getPuzzleWords() {
      return puzzleWords;
    }

    public void setPuzzleWords(
        List<PuzzleWord> puzzleWords) {
      this.puzzleWords = puzzleWords;
    }

    public static class PuzzleWord {

      @Expose private String clue;
      @Expose private Direction direction;

      public enum Direction {
        ACROSS,
        DOWN
      }

      @Expose private WordPosition wordPosition;


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

      public static class WordPosition {

        @Expose private int row;
        @Expose private int column;
        @Expose private int length;


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
      }

    }

  }


}
