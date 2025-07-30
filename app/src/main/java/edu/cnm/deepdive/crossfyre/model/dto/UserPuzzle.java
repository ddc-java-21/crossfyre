package edu.cnm.deepdive.crossfyre.model.dto;

import com.google.gson.annotations.Expose;
import java.time.Instant;
import java.util.List;
import java.util.UUID;


public class UserPuzzle {

//  @Expose
//  private long id;

  @Expose
  private final UUID userExternalKey = null;

  @Expose
  private final Instant created = null;

  @Expose
  private Boolean isSolved = false;

  private List<Guess> guesses;


  public UUID getUserExternalKey() {
    return userExternalKey;
  }

  public Instant getCreated() {
    return created;
  }

  public Boolean getIsSolved() {
    return isSolved;
  }

  public Boolean setSolved(Boolean solved) {
    this.isSolved = solved;
    return solved;
  }

  public List<Guess> getGuesses() {
    return guesses;
  }

  public UserPuzzle setGuesses(List<Guess> guesses) {
    this.guesses = guesses;
    return this;
  }

  public static class Guess {

    @Expose
    private char guessChar;

    private GuessPosition guessPosition;

    public char getGuessChar() {
      return guessChar;
    }

    public Guess setGuessChar(char guessChar) {
      this.guessChar = guessChar;
      return this;
    }

    public GuessPosition getGuessPosition() {
      return guessPosition;
    }

    public Guess setGuessPosition(GuessPosition guessPosition) {
      this.guessPosition = guessPosition;
      return this;
    }

    public static class GuessPosition {

      @Expose
      private int guessRow;

      @Expose
      private int guessColumn;

      public int getGuessRow() {
        return guessRow;
      }

      public GuessPosition setGuessRow(int guessRow) {
        this.guessRow = guessRow;
        return this;
      }

      public int getGuessColumn() {
        return guessColumn;
      }

      public GuessPosition setGuessColumn(int guessColumn) {
        this.guessColumn = guessColumn;
        return this;
      }
    }

    private Puzzle puzzle;

    public Puzzle getPuzzle() {
      return puzzle;
    }

    public Guess setPuzzle(Puzzle puzzle) {
      this.puzzle = puzzle;
      return this;
    }

    public static class Puzzle {

      @Expose
      private final UUID externaPuzzlelKey = null;

      @Expose
      private final int size = 5;

      public enum Board {
        SUNDAY    ("0__________________0____0"),
        MONDAY    ("0___00___0_______________"),
        TUESDAY   ("00_____________________00"),
        WEDNESDAY ("___00_______________00___"),
        THURSDAY  ("0___0_______________0___0"),
        FRIDAY    ("____0____0_____0____0____"),
        SATURDAY  ("___00____0_____0____00___");

        public final String day;

        Board(String day) {
          this.day = day;
        }

      }
//      @Expose
//      private final Board board = getPuzzleBoard();
    }


  }

}
