package edu.cnm.deepdive.crossfyre.model.dto;

import com.google.gson.annotations.Expose;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class UserPuzzle {

  @Expose
  private long id;

  @Expose
  private UUID userExternalKey = null;

  @Expose
  private final Instant created = null;

  @Expose
  private final Instant solved = null;

  private final User user = null;

  private List<Guess> guesses;

  public long getId() {
    return id;
  }


  public UUID getUserExternalKey() {
    return userExternalKey;
  }

  public UserPuzzle setUserExternalKey(UUID userExternalKey) {
    this.userExternalKey = userExternalKey;
    return this;
  }

  public Instant getCreated() {
    return created;
  }

  public Instant getSolved() {
    return solved;
  }

  public User getUser() {
    return user;
  }

  public List<Guess> getGuesses() {
    return guesses;
  }

  public UserPuzzle setGuesses(List<Guess> guesses) {
    this.guesses = guesses;
    return this;
  }
}
