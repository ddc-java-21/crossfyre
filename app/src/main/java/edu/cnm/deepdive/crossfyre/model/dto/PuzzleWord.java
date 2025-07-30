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

  public static class WordPosition {

    @Expose
    int wordRow;

    @Expose
    int wordColumn;

    @Expose
    int wordLength;


    public int getWordRow() {
      return wordRow;
    }

    public void setWordRow(int wordRow) {
      this.wordRow = wordRow;
    }

    public int getWordColumn() {
      return wordColumn;
    }

    public void setWordColumn(int wordColumn) {
      this.wordColumn = wordColumn;
    }

    public int getWordLength() {
      return wordLength;
    }

    public void setWordLength(int wordLength) {
      this.wordLength = wordLength;
    }

  }

  @Expose
  public WordPosition wordPosition;

  @Expose
  private Direction wordDirection;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getClue() {
    return clue;
  }

  public void setClue(String clue) {
    this.clue = clue;
  }

  public Direction getWordDirection() {
    return wordDirection;
  }

  public void setWordDirection(Direction wordDirection) {
    this.wordDirection = wordDirection;
  }

}
