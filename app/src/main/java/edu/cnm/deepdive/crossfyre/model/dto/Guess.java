package edu.cnm.deepdive.crossfyre.model.dto;

import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import java.time.Instant;
import java.util.UUID;

public class Guess {

  @Expose(serialize = false)
  private long id;

  @Expose(serialize = false)
  private GuessPosition guessPosition;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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
