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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

@SuppressWarnings({"JpaDataSourceORMInspection", "RedundantSuppression"})
@Entity
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"key", "player", "wordName", "posted"})
public class UserWord {

  private static final int MAX_MESSAGE_LENGTH = 255;

  @Id
  @GeneratedValue
  @Column(name = "user_word_id", nullable = false, updatable = false)
  @JsonIgnore
  private long id;

  @Column(nullable = false, updatable = false, unique = true)
  @JsonProperty(value = "key", access = Access.READ_ONLY)
  private UUID externalKey;

  @NotBlank
  @Length(max = MAX_MESSAGE_LENGTH)
  @Column(nullable = false, updatable = false, length = MAX_MESSAGE_LENGTH)
  private String wordName;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  @JsonProperty(access = Access.READ_ONLY)
  private Instant posted;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_puzzle_id", nullable = false, updatable = false)
  @JsonProperty(value = "puzzle", access = Access.READ_ONLY)
  private UserPuzzle userPuzzle;

  @Column(nullable = false, updatable = false, unique = true)
  private String clue;

  @Column(nullable = false, updatable = false)
  private int wordRow;

  @Column(nullable = false, updatable = false)
  private int wordColumn;

  @Column(nullable = false, updatable = false)
  private String wordDirection;

  public long getId() {
    return id;
  }

  public UUID getExternalKey() {
    return externalKey;
  }

  public String getWordName() {
    return wordName;
  }

  public void setWordName(String wordName) {
    this.wordName = wordName;
  }

  public Instant getPosted() {
    return posted;
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

  @PrePersist
  void generateFieldValues() {
    externalKey = UUID.randomUUID();
  }

}
