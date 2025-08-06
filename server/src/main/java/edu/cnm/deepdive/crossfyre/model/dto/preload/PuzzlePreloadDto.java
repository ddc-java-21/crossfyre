package edu.cnm.deepdive.crossfyre.model.dto.preload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) representing a puzzle and its associated metadata
 * for preloading into the application.
 */
@SuppressWarnings({"JpaDataSourceORMInspection", "RedundantSuppression"})
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"key", "size", "board", "created", "date", "puzzle_words"})
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
  private LocalDate date;

  @JsonProperty(value = "puzzle_words", access = Access.READ_WRITE)
  private List<PuzzleWord> puzzleWords = new LinkedList<>();

  /**
   * Returns the external UUID key for this puzzle.
   */
  public UUID getExternalKey() {
    return externalKey;
  }

  /**
   * Returns the puzzle size (e.g. grid size).
   */
  public int getSize() {
    return size;
  }

  /**
   * Sets the puzzle size.
   */
  public void setSize(int size) {
    this.size = size;
  }

  /**
   * Returns the enum representation of the puzzle board type (e.g. SUNDAY, MONDAY).
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Sets the puzzle board type.
   */
  public void setBoard(Board board) {
    this.board = board;
  }

  /**
   * Returns the creation timestamp of the puzzle.
   */
  public Instant getCreated() {
    return created;
  }

  /**
   * Sets the creation timestamp of the puzzle.
   */
  public void setCreated(Instant created) {
    this.created = created;
  }

  /**
   * Returns the associated calendar date of the puzzle.
   */
  public LocalDate getDate() {
    return date;
  }

  /**
   * Sets the date associated with the puzzle.
   */
  public void setDate(LocalDate date) {
    this.date = date;
  }

  /**
   * Returns the list of words used in this puzzle.
   */
  public List<PuzzleWord> getPuzzleWords() {
    return puzzleWords;
  }

  /**
   * Enum representing the type of puzzle board.
   */
  public enum Board {
    SUNDAY("0__________________0____0"),
    MONDAY("0___00___0_______________"),
    TUESDAY("00_____________________00"),
    WEDNESDAY("___00_______________00___"),
    THURSDAY("0___0_______________0___0"),
    FRIDAY("____0____0_____0____0____"),
    SATURDAY("___00____0_____0____00___");

    /**
     * Pattern string used to define the layout of black squares in the board.
     */
    public final String day;

    Board(String day) {
      this.day = day;
    }
  }

  /**
   * DTO representing a word in the puzzle, along with its position, clue, and direction.
   */
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

    /**
     * Returns the word string.
     */
    public String getWordName() {
      return wordName;
    }

    /**
     * Sets the word string.
     */
    public void setWordName(String wordName) {
      this.wordName = wordName;
    }

    /**
     * Returns the external UUID key for this word.
     */
    public UUID getExternalKey() {
      return externalKey;
    }

    /**
     * Sets the external UUID key for this word.
     */
    public void setExternalKey(UUID externalKey) {
      this.externalKey = externalKey;
    }

    /**
     * Returns the clue associated with this word.
     */
    public String getClue() {
      return clue;
    }

    /**
     * Sets the clue for this word.
     */
    public void setClue(String clue) {
      this.clue = clue;
    }

    /**
     * Returns the direction of the word (ACROSS or DOWN).
     */
    public Direction getDirection() {
      return direction;
    }

    /**
     * Sets the direction of the word.
     */
    public void setDirection(Direction direction) {
      this.direction = direction;
    }

    /**
     * Returns the word's position (row, column, and length).
     */
    public WordPosition getWordPosition() {
      return wordPosition;
    }

    /**
     * Sets the word's position.
     */
    public void setWordPosition(WordPosition wordPosition) {
      this.wordPosition = wordPosition;
    }

    /**
     * Enum representing the direction of a word.
     */
    public enum Direction {
      ACROSS,
      DOWN
    }

    /**
     * DTO representing the position of a word in the puzzle grid.
     */
    public static class WordPosition {

      @JsonProperty(value = "row", access = Access.READ_WRITE)
      private int row;

      @JsonProperty(value = "column", access = Access.READ_WRITE)
      private int column;

      @JsonProperty(value = "length", access = Access.READ_WRITE)
      private int length;

      /**
       * Returns the row index of the word.
       */
      public int getRow() {
        return row;
      }

      /**
       * Sets the row index of the word.
       */
      public void setRow(int row) {
        this.row = row;
      }

      /**
       * Returns the column index of the word.
       */
      public int getColumn() {
        return column;
      }

      /**
       * Sets the column index of the word.
       */
      public void setColumn(int column) {
        this.column = column;
      }

      /**
       * Returns the length of the word.
       */
      public int getLength() {
        return length;
      }

      /**
       * Sets the length of the word.
       */
      public void setLength(int length) {
        this.length = length;
      }
    }
  }
}
