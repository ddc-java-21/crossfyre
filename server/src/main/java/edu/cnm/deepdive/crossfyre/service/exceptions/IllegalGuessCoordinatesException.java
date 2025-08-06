package edu.cnm.deepdive.crossfyre.service.exceptions;

/**
 * Exception thrown when the coordinates of a guess are outside the bounds or invalid for the
 * current puzzle context.
 *
 * <p>This exception is typically used to indicate that a user or system has attempted to guess
 * a letter at a position that doesn't exist within the puzzle grid (e.g., negative coordinates,
 * or coordinates beyond the puzzle dimensions).</p>
 *
 * <p>This class extends {@link IllegalArgumentException}, making it suitable for use in
 * scenarios where invalid arguments (specifically, coordinates) are passed to methods.</p>
 */
public class IllegalGuessCoordinatesException extends IllegalArgumentException {

  /**
   * Constructs a new {@code IllegalGuessCoordinatesException} with no detail message.
   */
  public IllegalGuessCoordinatesException() {
    super();
  }

  /**
   * Constructs a new {@code IllegalGuessCoordinatesException} with the specified detail message.
   *
   * @param message The detail message explaining the reason for the exception.
   */
  public IllegalGuessCoordinatesException(String message) {
    super(message);
  }

  /**
   * Constructs a new {@code IllegalGuessCoordinatesException} with the specified cause.
   *
   * @param cause The cause of this exception (used for later retrieval).
   */
  public IllegalGuessCoordinatesException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructs a new {@code IllegalGuessCoordinatesException} with the specified detail message and cause.
   *
   * @param message The detail message explaining the reason for the exception.
   * @param cause   The cause of this exception (used for later retrieval).
   */
  public IllegalGuessCoordinatesException(String message, Throwable cause) {
    super(message, cause);
  }
}
