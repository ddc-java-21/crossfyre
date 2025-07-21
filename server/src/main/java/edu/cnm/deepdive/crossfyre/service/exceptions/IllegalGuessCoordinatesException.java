package edu.cnm.deepdive.crossfyre.service.exceptions;

public class IllegalGuessCoordinatesException extends IllegalArgumentException {

  public IllegalGuessCoordinatesException() {
    super();
  }

  public IllegalGuessCoordinatesException(String message) {
    super(message);
  }

  public IllegalGuessCoordinatesException(Throwable cause) {
    super(cause);
  }

  public IllegalGuessCoordinatesException(String message, Throwable cause) {
    super(message, cause);
  }
  
}
