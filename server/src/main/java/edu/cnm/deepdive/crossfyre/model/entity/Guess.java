package edu.cnm.deepdive.crossfyre.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Represents a user's single-character guess for a cell in a crossword puzzle grid.
 * A {@code Guess} is associated with a {@link UserPuzzle}, and contains the character guessed,
 * its grid location, and metadata such as creation time and a unique external key.
 */
@SuppressWarnings({"JpaDataSourceORMInspection", "RedundantSuppression"})
@Entity
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"puzzle", "clue", "row", "column", "guess"})
public class Guess {

  private static final int CHARACTER_GUESS_LIMIT = 1;

  /**
   * Unique internal database ID for this guess.
   */
  @Id
  @GeneratedValue
  @Column(name = "guess_id", nullable = false, updatable = false)
  @JsonIgnore
  private long id;

  /**
   * Unique external key for identifying this guess in APIs.
   */
  @Column(nullable = false, updatable = false, unique = true)
  @JsonProperty(value = "key", access = Access.READ_ONLY)
  private UUID externalKey;

  /**
   * The guessed character at the specified grid position.
   */
  @Column(nullable = false, updatable = true, length = CHARACTER_GUESS_LIMIT)
  @JsonProperty(value = "guess", access = Access.READ_WRITE)
  private Character guessChar;

  /**
   * Timestamp of when the guess was created. Automatically populated on insert.
   */
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  @JsonProperty(value = "created", access = Access.READ_ONLY)
  private Instant created;

  /**
   * Grid position (row and column) of the guess.
   */
  private GuessPosition guessPosition;

  /**
   * Reference to the {@link UserPuzzle} that this guess belongs to.
   */
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_puzzle_id", nullable = false)
  @JsonProperty(value = "puzzle", access = Access.READ_WRITE)
  @JsonIgnore
  private UserPuzzle userPuzzle;

  /**
   * @return Internal database ID of the guess.
   */
  public long getId() {
    return id;
  }

  /**
   * @return External UUID key for this guess.
   */
  public UUID getExternalKey() {
    return externalKey;
  }

  /**
   * @return Character guessed for this cell.
   */
  public Character getGuessChar() {
    return guessChar;
  }

  /**
   * Sets the character guessed for this cell.
   * @param guessChar Character to assign.
   */
  public void setGuessChar(Character guessChar) {
    this.guessChar = guessChar;
  }

  /**
   * @return Timestamp when the guess was created.
   */
  public Instant getCreated() {
    return created;
  }

  /**
   * Sets the creation timestamp of this guess.
   * Typically only used in testing or special cases.
   * @param created Creation timestamp.
   */
  public void setCreated(Instant created) {
    this.created = created;
  }

  /**
   * @return The {@link UserPuzzle} associated with this guess.
   */
  public UserPuzzle getUserPuzzle() {
    return userPuzzle;
  }

  /**
   * Sets the puzzle context for this guess.
   * @param userPuzzle UserPuzzle to associate with this guess.
   */
  public void setUserPuzzle(UserPuzzle userPuzzle) {
    this.userPuzzle = userPuzzle;
  }

  /**
   * @return The row and column position of the guess.
   */
  public GuessPosition getGuessPosition() {
    return guessPosition;
  }

  /**
   * Sets the row and column location of the guess.
   * @param guessPosition Grid position of this guess.
   */
  public void setGuessPosition(GuessPosition guessPosition) {
    this.guessPosition = guessPosition;
  }

  /**
   * Compares two {@code Guess} objects based on their internal ID.
   * @param obj Other object to compare.
   * @return {@code true} if the objects are the same or have matching IDs.
   */
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

  /**
   * @return Hash code derived from the internal ID.
   */
  @Override
  public int hashCode() {
    return Long.hashCode(id);
  }

  /**
   * Generates a random UUID for {@code externalKey} before persisting the entity.
   */
  @PrePersist
  void generateFieldValues() {
    externalKey = UUID.randomUUID();
  }

  /**
   * Represents the grid location (row and column) of a guess.
   * This is an embeddable JPA component.
   *
   * @param guessRow Row index of the guess.
   * @param guessColumn Column index of the guess.
   */
  @SuppressWarnings("JpaObjectClassSignatureInspection")
  @Embeddable
  public record GuessPosition(

      @Column(nullable = false, updatable = false)
      @JsonProperty(value = "row", access = Access.READ_ONLY)
      int guessRow,

      @Column(nullable = false, updatable = false)
      @JsonProperty(value = "column", access = Access.READ_ONLY)
      int guessColumn) {}

}
