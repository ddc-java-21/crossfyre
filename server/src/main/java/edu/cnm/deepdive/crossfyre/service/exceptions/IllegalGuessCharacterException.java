package edu.cnm.deepdive.crossfyre.service.exceptions;

public class IllegalGuessCharacterException extends IllegalArgumentException {

  public IllegalGuessCharacterException() {
    super();
  }

  public IllegalGuessCharacterException(String message) {
    super(message);
  }

  public IllegalGuessCharacterException(Throwable cause) {
    super(cause);
  }

  public IllegalGuessCharacterException(String message, Throwable cause) {
    super(message, cause);
  }
  
}
