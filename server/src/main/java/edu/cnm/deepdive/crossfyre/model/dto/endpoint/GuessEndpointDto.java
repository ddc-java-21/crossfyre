package edu.cnm.deepdive.crossfyre.model.dto.endpoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@SuppressWarnings({"JpaDataSourceORMInspection", "RedundantSuppression"})
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"guess", "guessPosition", "row", "column"})
public class GuessEndpointDto {

  @JsonProperty(value = "guess", access = Access.READ_WRITE)
  private String guess;

  @JsonProperty(value = "guessPosition", access = Access.READ_WRITE)
  private GuessPosition guessPosition;


  public String getGuess() {
    return guess;
  }

  public void setGuess(String guess) {
    this.guess = guess;
  }

  public GuessPosition getGuessPosition() {
    return guessPosition;
  }

  public void setGuessPosition(
      GuessPosition guessPosition) {
    this.guessPosition = guessPosition;
  }


  public static class GuessPosition {

    @JsonProperty(value = "row", access = Access.READ_WRITE)
    private int row;

    @JsonProperty(value = "column", access = Access.READ_WRITE)
    private int column;


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
