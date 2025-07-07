package edu.cnm.deepdive.crossfyre.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@SuppressWarnings({"JpaDataSourceORMInspection", "RedundantSuppression"})
@Entity
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"puzzle", "clue", "wordName", "row", "column", "direction"})
public class UserWord {

  private static final int MAX_MESSAGE_LENGTH = 25;

  @Id
  @GeneratedValue
  @Column(name = "user_word_id", nullable = false, updatable = false)
  @JsonIgnore
  private long id;

  @NotBlank
  @Length(max = MAX_MESSAGE_LENGTH)
  @Column(nullable = false, updatable = false)
  @JsonProperty(value = "wordName", access = Access.READ_WRITE)
  private String wordName;


  @Column(nullable = false, updatable = false, unique = true)
  @JsonProperty(value = "clue", access = Access.READ_ONLY)
  private String clue;

  @Column(nullable = false, updatable = false)
  @JsonProperty(value = "row", access = Access.READ_ONLY)
  private int wordRow;

  @Column(nullable = false, updatable = false)
  @JsonProperty(value = "column", access = Access.READ_ONLY)
  private int wordColumn;

  @Column(nullable = false, updatable = false)
  @JsonProperty(value = "direction", access = Access.READ_ONLY)
  private String wordDirection;
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_puzzle_id", nullable = false, updatable = true)
  @JsonProperty(value = "puzzle", access = Access.READ_WRITE)
  private UserPuzzle userPuzzle;

  public long getId() {
    return id;
  }

  public String getWordName() {
    return wordName;
  }

  public void setWordName(String wordName) {
    this.wordName = wordName;
  }

  public UserPuzzle getUserPuzzle() {
    return userPuzzle;
  }

  public void setUserPuzzle(UserPuzzle userPuzzle) {
    this.userPuzzle = userPuzzle;
  }

  public String getClue() {
    return clue;
  }

  public void setClue(String clue) {
    this.clue = clue;
  }

  public int getWordRow() {
    return wordRow;
  }

  public void setWordRow(int wordRow) {
    this.wordRow = wordRow;
  }

  public int getWordColumn() {
    return wordColumn;
  }

  public void setWordColumn(int wordColumn) {
    this.wordColumn = wordColumn;
  }

  public String getWordDirection() {
    return wordDirection;
  }

  public void setWordDirection(String wordDirection) {
    this.wordDirection = wordDirection;
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
    } else if (obj instanceof UserWord other) {
      comparison = (this.id != 0 && this.id == other.id);
    } else {
      comparison = false;
    }
    return comparison;
  }
}
