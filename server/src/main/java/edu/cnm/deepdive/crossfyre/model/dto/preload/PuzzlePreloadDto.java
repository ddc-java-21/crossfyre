package edu.cnm.deepdive.crossfyre.model.dto.preload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings({"JpaDataSourceORMInspection", "RedundantSuppression"})
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"key", "size", "board", "created, date, puzzleWords"})
public class PuzzlePreloadDto {

  @JsonProperty(value = "key", access = Access.READ_WRITE)
  private UUID externalKey;

  @JsonProperty(access = Access.READ_WRITE)
  private int size;

  @JsonProperty(value = "board", access = Access.READ_WRITE)
  private Board board;

  @JsonProperty(value = "created", access = Access.READ_WRITE)
  private Instant created;

  @JsonProperty(value = "date", access = Access.READ_WRITE)
  private Instant date;

  @JsonProperty(value = "puzzle_words", access = Access.READ_WRITE)
  private List<PuzzleWord> puzzleWords = new LinkedList<>(); // TN 2025-07-07 added puzzleWords list

  public static class PuzzleWord {

    @JsonProperty(value = "word_name", access = Access.READ_WRITE)
    private String wordName;

    @JsonProperty(value = "key", access = Access.READ_WRITE)
    private UUID externalKey;

    @JsonProperty(value = "clue", access = Access.READ_WRITE)
    private String clue;

    @JsonProperty(value = "direction", access = Access.READ_WRITE)
    private Direction direction;

    @JsonProperty(value = "word_position", access = Access.READ_WRITE)
    private WordPosition wordPosition;


    public String getWordName() {
      return wordName;
    }

    public void setWordName(String wordName) {
      this.wordName = wordName;
    }

    public UUID getExternalKey() {
      return externalKey;
    }

    public void setExternalKey(UUID externalKey) {
      this.externalKey = externalKey;
    }

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

    public enum Direction {
      ACROSS,
      DOWN
    }

    public static class WordPosition {

      @JsonProperty(value = "row", access = Access.READ_WRITE)
      int row;

      @JsonProperty(value = "column", access = Access.READ_WRITE)
      int column;

      @JsonProperty(value = "length", access = Access.READ_WRITE)
      int length;


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


  public UUID getExternalKey() {
    return externalKey;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
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

}
