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

@SuppressWarnings({"JpaDataSourceORMInspection", "RedundantSuppression"})
@Entity
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"puzzle", "clue", "wordName", "row", "column", "direction"})
public class Guess {

  private static final int CHARACTER_GUESS_LIMIT = 1;

  @Id
  @GeneratedValue
  @Column(name = "guess_id", nullable = false, updatable = false)
  @JsonIgnore
  private long id;

  @Column(nullable = false, updatable = true, length = CHARACTER_GUESS_LIMIT)
  @JsonProperty(value = "guess", access = Access.WRITE_ONLY)
  private Character guessChar;

  @Column(nullable = false, updatable = false)
  @JsonProperty(value = "row", access = Access.READ_ONLY)
  private int guessRow;

  @Column(nullable = false, updatable = false)
  @JsonProperty(value = "column", access = Access.READ_ONLY)
  private int guessColumn;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_puzzle_id", nullable = false, updatable = false)
  @JsonProperty(value = "puzzle", access = Access.READ_WRITE)
  private UserPuzzle userPuzzle;

  public long getId() {
    return id;
  }

  public UserPuzzle getUserPuzzle() {
    return userPuzzle;
  }

  public void setUserPuzzle(UserPuzzle userPuzzle) {
    this.userPuzzle = userPuzzle;
  }

  public int getGuessRow() {
    return guessRow;
  }

  public void setGuessRow(int wordRow) {
    this.guessRow = wordRow;
  }

  public int getGuessColumn() {
    return guessColumn;
  }

  public void setGuessColumn(int wordColumn) {
    this.guessColumn = wordColumn;
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
}
