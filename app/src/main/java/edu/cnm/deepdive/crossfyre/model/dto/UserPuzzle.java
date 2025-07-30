package edu.cnm.deepdive.crossfyre.model.dto;

import com.google.gson.annotations.Expose;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class UserPuzzle {

//  @Expose
//  private long id;

  @Expose
  private final UUID userExternalKey = null;

  @Expose
  private final Instant created = null;

  @Expose
  private Boolean isSolved = null;

  private List<Guess> guesses;

  public UUID getUserExternalKey() {
    return userExternalKey;
  }

  public Instant getCreated() {
    return created;
  }

  public Boolean getSolved() {
    return isSolved;
  }

  public UserPuzzle setSolved(Boolean solved) {
    isSolved = solved;
    return this;
  }

  public List<Guess> getGuesses() {
    return guesses;
  }

  public UserPuzzle setGuesses(List<Guess> guesses) {
    this.guesses = guesses;
    return this;
  }

}
