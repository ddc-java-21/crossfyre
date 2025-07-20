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
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import org.hibernate.validator.constraints.Length;

@SuppressWarnings({"JpaDataSourceORMInspection", "RedundantSuppression"})
@Entity
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"wordname", "clue", "row", "column", "direction", "puzzle"})
public class PuzzleWord {

  private static final int MAX_WORD_LENGTH = 255;

  @Id
  @GeneratedValue
  @Column(name = "puzzle_word_id", nullable = false, updatable = false)
  @JsonIgnore
  private long id;

  @Column(nullable = false, updatable = false, unique = true)
  @JsonProperty(value = "key", access = Access.READ_ONLY)
  private UUID externalKey;

  @NotBlank
  @Length(max = MAX_WORD_LENGTH)
  @Column(unique = true, nullable = false, updatable = false)
  @JsonIgnore
  private String wordName;

  @Column(nullable = false, updatable = false, unique = true)
  @JsonProperty(value = "clue", access = Access.READ_ONLY)
  private String clue;

  @Column(nullable = false, updatable = false)
  @JsonProperty(value = "column", access = Access.READ_ONLY)
  private int wordLength;

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
  @JoinColumn(name = "puzzle_id", nullable = false, updatable = false)
  @JsonProperty(value = "puzzle", access = Access.READ_ONLY)
  private Puzzle puzzle;


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

  public String getClue() {
    return clue;
  }

  public void setClue(String clue) {
    this.clue = clue;
  }

  public int getWordLength() {
    return wordLength;
  }

  public void setWordLength(int wordLength) {
    this.wordLength = wordLength;
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

  public Puzzle getPuzzle() {
    return puzzle;
  }

  public void setPuzzle(Puzzle puzzle) {
    this.puzzle = puzzle;
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
    } else if (obj instanceof PuzzleWord other) {
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
