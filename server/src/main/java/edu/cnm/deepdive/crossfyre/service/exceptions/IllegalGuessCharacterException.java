package edu.cnm.deepdive.crossfyre.service.exceptions;

/**
 * Exception thrown when a guess contains one or more characters that are not allowed in a puzzle.
 *
 * <p>This typically indicates an attempt to submit a guess that includes non-alphabetic,
 * special, or otherwise invalid characters for the puzzle context.</p>
 *
 * <p>This is a subclass of {@link IllegalArgumentException}, used to indicate that a method
 * has been passed an illegal or inappropriate argument.</p>
 */
public class IllegalGuessCharacterException extends IllegalArgumentException {

  /**
   * Constructs a new {@code IllegalGuessCharacterException} with no detail message.
   */
  public IllegalGuessCharacterException() {
    super();
  }

  /**
   * Constructs a new {@code IllegalGuessCharacterException} with the specified detail message.
   *
   * @param message The detail message explaining the reason for the exception.
   */
  public IllegalGuessCharacterException(String message) {
    super(message);
  }

  /**
   * Constructs a new {@code IllegalGuessCharacterException} with the specified cause.
   *
   * @param cause The cause of this exception (used for later retrieval).
   */
  public IllegalGuessCharacterException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructs a new {@code IllegalGuessCharacterException} with the specified detail message and cause.
   *
   * @param message The detail message explaining the reason for the exception.
   * @param cause   The cause of this exception (used for later retrieval).
   */
  public IllegalGuessCharacterException(String message, Throwable cause) {
    super(message, cause);
  }
}
