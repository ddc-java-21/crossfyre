package edu.cnm.deepdive.crossfyre.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import java.time.Instant;
import java.util.UUID;

@SuppressWarnings({"JpaDataSourceORMInspection", "RedundantSuppression"})
@Entity
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"puzzle", "clue", "row", "column", "guess"})
public class Guess {

  private static final int CHARACTER_GUESS_LIMIT = 1;

  @Id
  @GeneratedValue
  @Column(name = "guess_id", nullable = false, updatable = false)
  @JsonIgnore
  private long id;

  @Column(nullable = false, updatable = false, unique = true)
  @JsonProperty(value = "key", access = Access.READ_ONLY)
  private UUID externalKey;

  @Column(nullable = false, updatable = true, length = CHARACTER_GUESS_LIMIT)
  @JsonProperty(value = "guess", access = Access.READ_WRITE) // TN 2025-07-07 changed from WRITE.ONLY
  private Character guessChar;

  @Column(nullable = false, updatable = false)
  @JsonProperty(value = "created", access = Access.READ_ONLY)
  private Instant created;

  @SuppressWarnings("JpaObjectClassSignatureInspection")
  @Embeddable
  public record GuessPosition(

      @Column(nullable = false, updatable = false)
      @JsonProperty(value = "row", access = Access.READ_ONLY)
      int guessRow,

      @Column(nullable = false, updatable = false)
      @JsonProperty(value = "column", access = Access.READ_ONLY)
      int guessColumn){}

  private GuessPosition guessPosition;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_puzzle_id", nullable = false, updatable = false)
  @JsonProperty(value = "puzzle", access = Access.READ_WRITE)
  private UserPuzzle userPuzzle;


  public long getId() {
    return id;
  }

  public UUID getExternalKey() {
    return externalKey;
  }

  public Character getGuessChar() {
    return guessChar;
  }

  public void setGuessChar(Character guessChar) {
    this.guessChar = guessChar;
  }

  public Instant getCreated() {
    return created;
  }

  public void setCreated(Instant created) {
    this.created = created;
  }

  public UserPuzzle getUserPuzzle() {
    return userPuzzle;
  }

  public void setUserPuzzle(UserPuzzle userPuzzle) {
    this.userPuzzle = userPuzzle;
  }

  public GuessPosition getGuessPosition() {
    return guessPosition;
  }

  public void setGuessPosition(GuessPosition guessPosition) {
    this.guessPosition = guessPosition;
  }

  @Override
  public int hashCode() {
    return Long.hashCode(id);
  }

  @Override
  public boolean equals(Object obj) {
    boolean comparison;
    if (this == obj) {
      comparison = true;
    } else if (obj instanceof Guess other) {
      comparison = (this.id != 0 && this.id == other.id);
    } else {
      comparison = false;
    }
    return comparison;
  }

  @PrePersist
  void generateFieldValues() {
    externalKey = UUID.randomUUID();
  }

}
