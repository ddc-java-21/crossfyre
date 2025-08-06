package edu.cnm.deepdive.crossfyre.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Represents the association of a User with a Puzzle,
 * tracking the user's progress and guesses for that puzzle.
 */
@SuppressWarnings({"JpaDataSourceORMInspection", "RedundantSuppression"})
@Entity
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"key", "created", "solved", "isSolved"})
public class UserPuzzle {

  /**
   * Internal database identifier for the user-puzzle association (not exposed externally).
   */
  @Id
  @GeneratedValue
  @Column(name = "user_puzzle_id", nullable = false, updatable = false)
  @JsonIgnore
  private long id;

  /**
   * External UUID key used to publicly identify this user-puzzle record.
   */
  @Column(nullable = false, updatable = false, unique = true)
  @JsonProperty(value = "key", access = Access.READ_ONLY)
  private UUID externalKey;

  /**
   * Timestamp when this user-puzzle record was created (auto-generated).
   */
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  @JsonProperty(value = "created", access = Access.READ_ONLY)
  private Instant created;

  /**
   * Timestamp when the puzzle was solved by the user (nullable).
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = true, updatable = true)
  @JsonProperty(value = "solved", access = Access.READ_WRITE)
  private Instant solved;

  /**
   * List of guesses made by the user on this puzzle.
   */
  @OneToMany(mappedBy =
      "userPuzzle", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Guess> guesses = new LinkedList<>();

  /**
   * The puzzle associated with this user-puzzle record.
   */
  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "puzzle_id", nullable = false, updatable = false)
  @JsonProperty(value = "puzzle", access = Access.READ_ONLY)
  private Puzzle puzzle;

  /**
   * The user associated with this user-puzzle record.
   */
  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "user_id", nullable = false, updatable = false)
  @JsonProperty(value = "user", access = Access.READ_ONLY)
  @JsonIgnore
  private User user;

  /**
   * Flag indicating whether the puzzle has been solved by the user.
   */
  @Column(nullable = false, updatable = false)
  @JsonProperty(value = "is_solved", access = Access.READ_ONLY)
  private boolean isSolved;

  /**
   * Returns the internal database ID of this UserPuzzle.
   *
   * @return internal ID.
   */
  public long getId() {
    return id;
  }

  /**
   * Returns the external UUID key of this UserPuzzle.
   *
   * @return external UUID key.
   */
  public UUID getExternalKey() {
    return externalKey;
  }

  /**
   * Returns the timestamp when this UserPuzzle was created.
   *
   * @return creation timestamp.
   */
  public Instant getCreated() {
    return created;
  }

  /**
   * Returns the list of guesses made by the user on this puzzle.
   *
   * @return list of guesses.
   */
  public List<Guess> getGuesses() {
    return guesses;
  }

  /**
   * Sets the list of guesses for this user-puzzle record.
   *
   * @param guesses list of guesses.
   */
  public void setGuesses(List<Guess> guesses) {
    this.guesses = guesses;
  }

  /**
   * Returns the puzzle associated with this record.
   *
   * @return the puzzle.
   */
  public Puzzle getPuzzle() {
    return puzzle;
  }

  /**
   * Sets the puzzle for this user-puzzle record.
   *
   * @param puzzle the puzzle to set.
   */
  public void setPuzzle(Puzzle puzzle) {
    this.puzzle = puzzle;
  }

  /**
   * Returns the user associated with this record.
   *
   * @return the user.
   */
  public User getUser() {
    return user;
  }

  /**
   * Sets the user for this user-puzzle record.
   *
   * @param user the user to set.
   */
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Returns the timestamp when the puzzle was solved by the user.
   *
   * @return the solved timestamp, or null if unsolved.
   */
  public Instant getSolved() {
    return solved;
  }

  /**
   * Sets the solved timestamp.
   *
   * @param solved timestamp when puzzle was solved.
   */
  public void setSolved(Instant solved) {
    this.solved = solved;
  }

  /**
   * Indicates whether this puzzle has been solved by the user.
   *
   * @return true if solved, false otherwise.
   */
  public boolean isSolved() {
    return isSolved;
  }

  /**
   * Sets the solved status flag.
   *
   * @param solved true if puzzle is solved, false otherwise.
   */
  public void setSolved(boolean solved) {
    isSolved = solved;
  }

  /**
   * Computes hash code based on internal ID.
   *
   * @return hash code.
   */
  @Override
  public int hashCode() {
    return Long.hashCode(id);
  }

  /**
   * Compares this UserPuzzle to another object for equality based on ID.
   *
   * @param obj the object to compare.
   * @return true if equal, false otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    boolean comparison;
    if (this == obj) {
      comparison = true;
    } else if (obj instanceof UserPuzzle other) {
      comparison = (this.id != 0 && this.id == other.id);
    } else {
      comparison = false;
    }
    return comparison;
  }

  /**
   * Lifecycle method to generate the external UUID key before persistence.
   */
  @PrePersist
  void generateFieldValues() {
    externalKey = UUID.randomUUID();
  }

}
