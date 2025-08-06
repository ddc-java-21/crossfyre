package edu.cnm.deepdive.crossfyre.model.dto.endpoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Data Transfer Object (DTO) representing a user's guess input in the puzzle.
 * This includes the guessed character and its position on the puzzle grid.
 *
 * <p>Only non-null fields will be included during JSON serialization.
 * Field access for JSON is read-write.</p>
 */
@SuppressWarnings({"JpaDataSourceORMInspection", "RedundantSuppression"})
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"guess", "guessPosition", "row", "column"})
public class GuessEndpointDto {

  /**
   * The letter or character guessed by the user.
   */
  @JsonProperty(value = "guess", access = Access.READ_WRITE)
  private String guess;

  /**
   * The position (row and column) of the guessed character on the board.
   */
  @JsonProperty(value = "guessPosition", access = Access.READ_WRITE)
  private GuessPosition guessPosition;

  /**
   * Returns the guessed character.
   *
   * @return the guessed character as a {@link String}.
   */
  public String getGuess() {
    return guess;
  }

  /**
   * Sets the guessed character.
   *
   * @param guess the guessed character to set.
   */
  public void setGuess(String guess) {
    this.guess = guess;
  }

  /**
   * Returns the position of the guess on the puzzle board.
   *
   * @return the {@link GuessPosition} object containing row and column.
   */
  public GuessPosition getGuessPosition() {
    return guessPosition;
  }

  /**
   * Sets the position of the guess on the puzzle board.
   *
   * @param guessPosition the {@link GuessPosition} to set.
   */
  public void setGuessPosition(GuessPosition guessPosition) {
    this.guessPosition = guessPosition;
  }

  /**
   * Represents a position on the puzzle board, specified by row and column.
   */
  public static class GuessPosition {

    /**
     * The row index of the guessed character (typically 0-based).
     */
    @JsonProperty(value = "row", access = Access.READ_WRITE)
    private int row;

    /**
     * The column index of the guessed character (typically 0-based).
     */
    @JsonProperty(value = "column", access = Access.READ_WRITE)
    private int column;

    /**
     * Returns the row index.
     *
     * @return the row index.
     */
    public int getRow() {
      return row;
    }

    /**
     * Sets the row index.
     *
     * @param row the row index to set.
     */
    public void setRow(int row) {
      this.row = row;
    }

    /**
     * Returns the column index.
     *
     * @return the column index.
     */
    public int getColumn() {
      return column;
    }

    /**
     * Sets the column index.
     *
     * @param column the column index to set.
     */
    public void setColumn(int column) {
      this.column = column;
    }
  }
}
