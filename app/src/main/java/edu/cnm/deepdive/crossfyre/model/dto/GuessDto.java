package edu.cnm.deepdive.crossfyre.model.dto;

import com.google.gson.annotations.Expose;

public class GuessDto {

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

  public void setGuessPosition(GuessPosition guessPosition) {
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
