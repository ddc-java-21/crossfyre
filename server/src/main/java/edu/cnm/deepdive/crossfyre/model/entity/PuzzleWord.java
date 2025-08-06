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

/**
 * Entity representing a word within a crossword puzzle.
 * Contains the word, its clue, position, direction, and association to a puzzle.
 */
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

  /**
   * Enum defining the direction of a word within a puzzle.
   */
  public enum Direction {
    ACROSS,
    DOWN
  }

  private static final int MAX_WORD_LENGTH = 255;

  /**
   * Internal database identifier for the puzzle word (not exposed externally).
   */
  @Id
  @GeneratedValue
  @Column(name = "puzzle_word_id", nullable = false, updatable = false)
  @JsonIgnore
  private long id;

  /**
   * Externally visible unique key for the puzzle word.
   */
  @Column(nullable = false, updatable = false, unique = true)
  @JsonProperty(value = "key", access = Access.READ_ONLY)
  private UUID externalKey;

  /**
   * The word itself in the puzzle.
   * Must be non-blank and no longer than 255 characters.
   * Not exposed in JSON.
   */
  @NotBlank
  @Length(max = MAX_WORD_LENGTH)
  @Column(nullable = false, updatable = false)
  @JsonIgnore
  private String wordName;

  /**
   * The clue text associated with the word.
   * Exposed as read-only in JSON.
   */
  @Column(nullable = false, updatable = false)
  @JsonProperty(value = "clue", access = Access.READ_ONLY)
  private String clue;

  /**
   * Immutable record representing the position of the word in the puzzle grid.
   * Includes the starting row, column, and length of the word.
   */
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
  ) {}

  /**
   * The position of this word in the puzzle grid.
   */
  private WordPosition wordPosition;

  /**
   * The direction of the word within the puzzle grid (ACROSS or DOWN).
   */
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, updatable = false)
  @JsonProperty(value = "direction", access = JsonProperty.Access.READ_ONLY)
  private Direction wordDirection;

  /**
   * Many-to-one relationship linking this word to the owning puzzle.
   * Marked to ignore in JSON serialization.
   */
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "puzzle_id", nullable = false, updatable = false)
  @JsonIgnore
  private Puzzle puzzle;

  /**
   * Gets the internal database ID of the puzzle word.
   *
   * @return the puzzle word database id.
   */
  public long getId() {
    return id;
  }

  /**
   * Gets the external UUID key for this puzzle word.
   *
   * @return the external unique identifier.
   */
  public UUID getExternalKey() {
    return externalKey;
  }

  /**
   * Gets the word as it appears in the puzzle.
   *
   * @return the puzzle word.
   */
  public String getWordName() {
    return wordName;
  }

  /**
   * Sets the word name.
   *
   * @param wordName the puzzle word text.
   */
  public void setWordName(String wordName) {
    this.wordName = wordName;
  }

  /**
   * Gets the associated puzzle entity.
   *
   * @return the puzzle this word belongs to.
   */
  public Puzzle getPuzzle() {
    return puzzle;
  }

  /**
   * Sets the puzzle this word belongs to.
   *
   * @param puzzle the owning puzzle entity.
   */
  public void setPuzzle(Puzzle puzzle) {
    this.puzzle = puzzle;
  }

  /**
   * Gets the clue text for this word.
   *
   * @return the clue.
   */
  public String getClue() {
    return clue;
  }

  /**
   * Sets the clue text.
   *
   * @param clue the clue associated with the word.
   */
  public void setClue(String clue) {
    this.clue = clue;
  }

  /**
   * Gets the word position record representing row, column, and length.
   *
   * @return the word position.
   */
  public WordPosition getWordPosition() {
    return wordPosition;
  }

  /**
   * Sets the position of the word in the puzzle grid.
   *
   * @param wordPosition the position (row, column, length).
   */
  public void setWordPosition(WordPosition wordPosition) {
    this.wordPosition = wordPosition;
  }

  /**
   * Gets the direction (ACROSS or DOWN) of the word.
   *
   * @return the word direction.
   */
  public Direction getWordDirection() {
    return wordDirection;
  }

  /**
   * Sets the direction of the word in the puzzle.
   *
   * @param wordDirection the direction (ACROSS or DOWN).
   */
  public void setWordDirection(Direction wordDirection) {
    this.wordDirection = wordDirection;
  }

  /**
   * Computes a hash code based on the internal ID.
   *
   * @return the hash code.
   */
  @Override
  public int hashCode() {
    return Long.hashCode(id);
  }

  /**
   * Compares this PuzzleWord to another object for equality based on ID.
   *
   * @param obj the object to compare to.
   * @return true if the objects are equal, false otherwise.
   */
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

  /**
   * Lifecycle callback to generate the external key before persisting.
   */
  @PrePersist
  void generateFieldValues() {
    externalKey = UUID.randomUUID();
  }

}
