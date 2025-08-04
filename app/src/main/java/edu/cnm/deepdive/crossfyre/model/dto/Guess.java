package edu.cnm.deepdive.crossfyre.model.dto;

import com.google.gson.annotations.Expose;

public class Guess {

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

  public void setGuessPosition(GuessPosition guessPosition) {
    this.guessPosition = guessPosition;
  }


  public static class GuessPosition {

    @Expose(serialize = false)
    int row;

    @Expose(serialize = false)
    int column;


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
