package edu.cnm.deepdive.crossfyre.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import org.hibernate.validator.constraints.Length;

@SuppressWarnings({"JpaDataSourceORMInspection", "RedundantSuppression"})
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"puzzle_id", "word_name"})
    }
)
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"word_name", "clue", "row", "column", "direction", "puzzle"})
public class PuzzleWord {

  // TODO: 7/15/2025 enum declaration
  public enum Direction {
    ACROSS,
    DOWN
  }

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
  //@JsonProperty(value = "word_name", access = Access.READ_ONLY)
  @JsonIgnore
  private String wordName;

  @Column(nullable = false, updatable = false, unique = true)
  @JsonProperty(value = "clue", access = Access.READ_ONLY)
  private String clue;

  //Consider adding length to record?
  @SuppressWarnings("JpaObjectClassSignatureInspection")
  @Embeddable
  public record WordPosition(
      @Column(nullable = false, updatable = false)
      @JsonProperty(value = "row", access = Access.READ_ONLY)
      int wordRow,

      @Column(nullable = false, updatable = false)
      @JsonProperty(value = "column", access = Access.READ_ONLY)
      int wordColumn,

      @Column(nullable = false, updatable = false)
      @JsonProperty(value = "length", access = Access.READ_ONLY)
      int wordLength
    ){}

  private WordPosition wordPosition;

  // TODO: 7/15/2025 Check enumerated/enum declaration
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, updatable = false)
  @JsonProperty(value = "direction", access = JsonProperty.Access.READ_ONLY)
  private Direction wordDirection;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "puzzle_id", nullable = false, updatable = false)
  //@JsonProperty(value = "puzzle", access = Access.READ_ONLY)
  @JsonIgnore
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

  public Puzzle getPuzzle() {
    return puzzle;
  }

  public void setPuzzle(Puzzle puzzle) {
    this.puzzle = puzzle;
  }

  public String getClue() {
    return clue;
  }

  public void setClue(String clue) {
    this.clue = clue;
  }

  public Direction getWordDirection() {
    return wordDirection;
  }

  public void setWordDirection(Direction wordDirection) {
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
