package edu.cnm.deepdive.crossfyre.model.dto;

import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import java.util.UUID;

public class Guess {

  // TODO: 7/17/2025 add Direction for possible hints?* 
  @Expose(serialize = false)
  private long id;

  @Expose(serialize = false)
  private int row;

  @Expose(serialize = false)
  private int col;

  private char guessChar;

  private UUID userPuzzleExternalKey;

  public long getId() {
    return id;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public char getGuessChar() {
    return guessChar;
  }

  public UUID getUserPuzzleExternalKey() {
    return userPuzzleExternalKey;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public void setCol(int col) {
    this.col = col;
  }

  public void setGuessChar(char guessChar) {
    this.guessChar = guessChar;
  }

}
