package edu.cnm.deepdive.crossfyre.model.dto;

import com.google.gson.annotations.Expose;
import edu.cnm.deepdive.crossfyre.model.dto.Guess.guessPosition;

public class PuzzleWord {

  public enum Direction {
    ACROSS,
    DOWN
  }

  @Expose
  private long id;

  @Expose
  private String clue;

  public record wordPosition(
      @Expose
      int wordRow,

      @Expose
      int wordColumn,

      @Expose
      int wordLength){}

  wordPosition wordPosition;

  @Expose
  private Direction wordDirection;

  public long getId() {
    return id;
  }

  public PuzzleWord setId(long id) {
    this.id = id;
    return this;
  }

  public String getClue() {
    return clue;
  }

  public PuzzleWord setClue(String clue) {
    this.clue = clue;
    return this;
  }

  public Direction getWordDirection() {
    return wordDirection;
  }

  public PuzzleWord setWordDirection(
      Direction wordDirection) {
    this.wordDirection = wordDirection;
    return this;
  }
}
