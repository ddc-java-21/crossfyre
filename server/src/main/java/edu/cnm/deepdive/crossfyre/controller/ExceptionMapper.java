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

@RestControllerAdvice
public class ExceptionMapper {

  @ExceptionHandler(NoSuchElementException.class)
  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Specified user or puzzle for date requested not found.")
  public void notFound() {}

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Specified user or puzzle for date requested not found.")
  public void entityNotFound() {}

  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(code = HttpStatus.CONFLICT, reason = "Resource already exists in database.")
  public void dataIntegrityViolation() {}

  @ExceptionHandler(CannotGetJdbcConnectionException.class)
  @ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Database is currently unavailable. Please try again later.")
  public void cannotGetJdbcConnection() {}

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Please review request and try again.")
  public void badRequest() {}

  @ExceptionHandler(HttpClientErrorException.class)
  @ResponseStatus(code = HttpStatus.TOO_MANY_REQUESTS, reason = "An error occurred resulting from too many requests. Please wait and try again.")
  public void dataAccessError() {}

  @ExceptionHandler(PersistenceException.class)
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "An error occurred with db persistence. Please try again.")
  public void persistenceError() {}

  @ExceptionHandler(EntityExistsException.class)
  @ResponseStatus(code = HttpStatus.CONFLICT, reason = "DB error: entity already exists in database.")
  public void entityExists() {}

  @ExceptionHandler(NoResultException.class)
  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Query failed to return expected result.")
  public void noResult() {}

  @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Query resulted in return resource of unexpected size.")
  public void incorrectResultSizeDataAccess() {}

  @ExceptionHandler(DataAccessException.class)
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "An error occurred with data access.")
  public void tooManyRequests() {}

  @ExceptionHandler(Exception.class)
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "An unexpected error occurred.")
  public void internalServerError() {}

  // Custom Exceptions
  @ExceptionHandler(CrosswordAlreadySolvedException.class)
  @ResponseStatus(code = HttpStatus.CONFLICT, reason = "Crossword is already solved; no more moves can be made.")
  public void crosswordAlreadySolved() {}

  @ExceptionHandler(IllegalGuessCharacterException.class)
  @ResponseStatus(code = HttpStatus.CONFLICT, reason = "Guess character must be alphabetic [A-Za-z].")
  public void illegalGuessCharacter() {}

  @ExceptionHandler(IllegalGuessCoordinatesException.class)
  @ResponseStatus(code = HttpStatus.CONFLICT, reason = "Guess coordinates out of bounds or otherwise invalid.")
  public void illegalGuessCoordinates() {}

}