package edu.cnm.deepdive.crossfyre.model.dto;

import com.google.gson.annotations.Expose;

public class PuzzleWord {

  public enum Direction {
    ACROSS,
    DOWN
  }

  @Expose
  private long id;

  @Expose
  private String clue;

  @Expose
  private int wordRow;

  @Expose
  private int wordColumn;

  @Expose
  private Direction wordDirection;

  @Expose
  private int wordLength;


  public String getClue() {
    return clue;
  }

  public int getWordRow() {
    return wordRow;
  }

  public int getWordColumn() {
    return wordColumn;
  }

  public Direction getWordDirection() {
    return wordDirection;
  }
}
