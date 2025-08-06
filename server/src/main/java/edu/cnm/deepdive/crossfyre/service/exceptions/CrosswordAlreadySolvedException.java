package edu.cnm.deepdive.crossfyre.service.exceptions;

/**
 * Exception thrown when an attempt is made to modify or submit a crossword puzzle that has already
 * been solved.
 *
 * <p>This is a subclass of {@link IllegalStateException}, indicating that the operation is
 * invalid given the current state of the puzzle.</p>
 */
public class CrosswordAlreadySolvedException extends IllegalStateException {

  /**
   * Constructs a new {@code CrosswordAlreadySolvedException} with no detail message.
   */
  public CrosswordAlreadySolvedException() {
    super();
  }

  /**
   * Constructs a new {@code CrosswordAlreadySolvedException} with the specified detail message.
   *
   * @param message The detail message explaining the reason for the exception.
   */
  public CrosswordAlreadySolvedException(String message) {
    super(message);
  }

  /**
   * Constructs a new {@code CrosswordAlreadySolvedException} with the specified cause.
   *
   * @param cause The cause of this exception (which is saved for later retrieval).
   */
  public CrosswordAlreadySolvedException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructs a new {@code CrosswordAlreadySolvedException} with the specified detail message and cause.
   *
   * @param message The detail message explaining the reason for the exception.
   * @param cause   The cause of this exception (which is saved for later retrieval).
   */
  public CrosswordAlreadySolvedException(String message, Throwable cause) {
    super(message, cause);
  }

}
