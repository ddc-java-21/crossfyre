package edu.cnm.deepdive.crossfyre.model.dto;

import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import java.time.Instant;
import java.util.UUID;

public class Guess {

  // TODO: 7/17/2025 add Direction for possible hints?* 
  @Expose(serialize = false)
  private long id;

  public record guessPosition(
      @Expose(serialize = false)
      int guessRow,

      @Expose(serialize = false)
      int guessColumn){}

  guessPosition guessPosition;

  private char guessChar;

  private Instant created;

  private UUID userPuzzleExternalKey;

  public long getId() {
    return id;
  }

  public char getGuessChar() {
    return guessChar;
  }

  public UUID getUserPuzzleExternalKey() {
    return userPuzzleExternalKey;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setGuessChar(char guessChar) {
    this.guessChar = guessChar;
  }

  public Instant getCreated() {
    return created;
  }

  public void setCreated(Instant created) {
    this.created = created;
  }

}
