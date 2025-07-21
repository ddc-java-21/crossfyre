package edu.cnm.deepdive.crossfyre.service.exceptions;

public class CrosswordAlreadySolvedException extends IllegalStateException {

  public CrosswordAlreadySolvedException() {
    super();
  }

  public CrosswordAlreadySolvedException(String message) {
    super(message);
  }

  public CrosswordAlreadySolvedException(Throwable cause) {
    super(cause);
  }

  public CrosswordAlreadySolvedException(String message, Throwable cause) {
    super(message, cause);
  }

}
