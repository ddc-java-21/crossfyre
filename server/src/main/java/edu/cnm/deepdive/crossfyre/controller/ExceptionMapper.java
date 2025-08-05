package edu.cnm.deepdive.crossfyre.controller;

import edu.cnm.deepdive.crossfyre.service.exceptions.CrosswordAlreadySolvedException;
import edu.cnm.deepdive.crossfyre.service.exceptions.IllegalGuessCharacterException;
import edu.cnm.deepdive.crossfyre.service.exceptions.IllegalGuessCoordinatesException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import java.util.NoSuchElementException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Maps exceptions thrown in the application to appropriate HTTP response status codes.
 * This controller advice centralizes exception handling for all REST controllers in the application,
 * ensuring consistent and user-friendly error messages and status codes.
 */
@RestControllerAdvice
public class ExceptionMapper {

  /**
   * Handles {@link NoSuchElementException} when a user or puzzle for a specific date is not found.
   */
  @ExceptionHandler(NoSuchElementException.class)
  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Specified user or puzzle for date requested not found.")
  public void notFound() {}

  /**
   * Handles {@link EntityNotFoundException} when a JPA entity is not found in the database.
   */
  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Specified user or puzzle for date requested not found.")
  public void entityNotFound() {}

  /**
   * Handles {@link DataIntegrityViolationException} when a unique or foreign key constraint is violated.
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(code = HttpStatus.CONFLICT, reason = "Resource already exists in database.")
  public void dataIntegrityViolation() {}

  /**
   * Handles {@link CannotGetJdbcConnectionException} when the database is unreachable.
   */
  @ExceptionHandler(CannotGetJdbcConnectionException.class)
  @ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Database is currently unavailable. Please try again later.")
  public void cannotGetJdbcConnection() {}

  /**
   * Handles {@link IllegalArgumentException} for bad client input.
   */
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Please review request and try again.")
  public void badRequest() {}

  /**
   * Handles {@link HttpClientErrorException} for rate limiting or external request issues.
   */
  @ExceptionHandler(HttpClientErrorException.class)
  @ResponseStatus(code = HttpStatus.TOO_MANY_REQUESTS, reason = "An error occurred resulting from too many requests. Please wait and try again.")
  public void dataAccessError() {}

  /**
   * Handles {@link PersistenceException} for general JPA persistence errors.
   */
  @ExceptionHandler(PersistenceException.class)
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "An error occurred with db persistence. Please try again.")
  public void persistenceError() {}

  /**
   * Handles {@link EntityExistsException} when trying to persist an already existing entity.
   */
  @ExceptionHandler(EntityExistsException.class)
  @ResponseStatus(code = HttpStatus.CONFLICT, reason = "DB error: entity already exists in database.")
  public void entityExists() {}

  /**
   * Handles {@link NoResultException} when a query fails to return a result.
   */
  @ExceptionHandler(NoResultException.class)
  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Query failed to return expected result.")
  public void noResult() {}

  /**
   * Handles {@link IncorrectResultSizeDataAccessException} when a query returns more or fewer results than expected.
   */
  @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Query resulted in return resource of unexpected size.")
  public void incorrectResultSizeDataAccess() {}

  /**
   * Handles generic {@link DataAccessException} for any other data access issues.
   */
  @ExceptionHandler(DataAccessException.class)
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "An error occurred with data access.")
  public void tooManyRequests() {}

  /**
   * Handles any unhandled {@link Exception} to prevent leaking stack traces to clients.
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "An unexpected error occurred.")
  public void internalServerError() {}

  // ───── Custom Application Exceptions ─────

  /**
   * Handles {@link CrosswordAlreadySolvedException} when a user tries to submit a move after the puzzle is already solved.
   */
  @ExceptionHandler(CrosswordAlreadySolvedException.class)
  @ResponseStatus(code = HttpStatus.CONFLICT, reason = "Crossword is already solved; no more moves can be made.")
  public void crosswordAlreadySolved() {}

  /**
   * Handles {@link IllegalGuessCharacterException} when a guess contains invalid characters.
   */
  @ExceptionHandler(IllegalGuessCharacterException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Guess character must be alphabetic [A-Za-z].")
  public void illegalGuessCharacter() {}

  /**
   * Handles {@link IllegalGuessCoordinatesException} when guess coordinates are invalid or out of bounds.
   */
  @ExceptionHandler(IllegalGuessCoordinatesException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Guess coordinates out of bounds or otherwise invalid.")
  public void illegalGuessCoordinates() {}

}
