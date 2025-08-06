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
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;


/**
 * Represents a crossword puzzle entity in the Crossfyre application.
 * Stores metadata and relationships with users and puzzle words.
 */
@Entity
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"key", "size", "board", "created, date, puzzleWords"})
public class Puzzle {

  /**
   * Primary database identifier for the puzzle (not exposed externally).
   */
  @Id
  @GeneratedValue
  @Column(name = "puzzle_id", nullable = false, updatable = false)
  @JsonIgnore
  private long id;

  /**
   * Externally visible unique identifier for the puzzle.
   */
  @Column(nullable = false, updatable = false, unique = true)
  @JsonProperty(value = "key", access = Access.READ_ONLY)
  private UUID externalKey;

  /**
   * Size of the puzzle grid (e.g., 5 for 5x5).
   */
  @Column(nullable = false, updatable = false)
  @JsonProperty(access = Access.READ_ONLY)
  private int size;

  /**
   * Day-of-week-style template board used to generate this puzzle.
   */
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, updatable = false)
  private Board board;

  /**
   * Timestamp for when the puzzle was created (automatically generated).
   */
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  @JsonProperty(value = "created", access = Access.READ_ONLY)
  private Instant created;

  /**
   * Date associated with this puzzle (e.g., publication date).
   */
  @Temporal(TemporalType.DATE)
  @Column(nullable = true, updatable = true)
  @JsonProperty(value = "date", access = Access.READ_WRITE)
  private LocalDate date;

  /**
   * List of user-puzzle join records representing user interactions with this puzzle.
   */
  @OneToMany(mappedBy = "puzzle", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List<UserPuzzle> userPuzzles = new LinkedList<>();

  /**
   * List of words and clue data contained within this puzzle.
   */
  @OneToMany(mappedBy = "puzzle", fetch = FetchType.LAZY,
      cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonProperty(value = "puzzleWords", access = Access.READ_ONLY)
  private final List<PuzzleWord> puzzleWords = new ArrayList<>();

  /**
   * Gets the internal ID of the puzzle.
   */
  public long getId() {
    return id;
  }

  /**
   * Gets the UUID (external key) of the puzzle.
   */
  public UUID getExternalKey() {
    return externalKey;
  }

  /**
   * Gets the puzzle's size.
   */
  public int getSize() {
    return size;
  }

  /**
   * Sets the puzzle's size.
   *
   * @param size Grid size (e.g., 5 for 5x5).
   */
  public void setSize(int size) {
    this.size = size;
  }

  /**
   * Gets the template board used to generate this puzzle.
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Sets the board template used for this puzzle.
   *
   * @param board A predefined board layout.
   */
  public void setBoard(Board board) {
    this.board = board;
  }

  /**
   * Gets the timestamp of when the puzzle was created.
   */
  public Instant getCreated() {
    return created;
  }

  /**
   * Manually sets the created timestamp (not recommended except for testing).
   */
  public void setCreated(Instant created) {
    this.created = created;
  }

  /**
   * Gets the logical or intended date associated with the puzzle.
   */
  public LocalDate getDate() {
    return date;
  }

  /**
   * Sets the logical or intended date for this puzzle.
   *
   * @param date A {@link LocalDate} for the puzzle.
   */
  public void setDate(LocalDate date) {
    this.date = date;
  }

  /**
   * Returns the list of {@link UserPuzzle} records linked to this puzzle.
   */
  public List<UserPuzzle> getUserPuzzles() {
    return userPuzzles;
  }

  /**
   * Returns the list of {@link PuzzleWord}s associated with this puzzle.
   */
  public List<PuzzleWord> getPuzzleWords() {
    return puzzleWords;
  }

  /**
   * Generates a hash code based on the puzzle's internal ID.
   */
  @Override
  public int hashCode() {
    return Long.hashCode(id);
  }

  /**
   * Compares this puzzle with another object for equality, based on ID.
   */
  @Override
  public boolean equals(Object obj) {
    boolean comparison;
    if (this == obj) {
      comparison = true;
    } else if (obj instanceof Puzzle other) {
      comparison = (this.id != 0 && this.id == other.id);
    } else {
      comparison = false;
    }
    return comparison;
  }

  /**
   * Initializes field values before the entity is persisted.
   * Specifically, generates a random UUID as the external key.
   */
  @PrePersist
  void generateFieldValues() {
    externalKey = UUID.randomUUID();
  }

  /**
   * Enum representing predefined board layouts used to construct puzzles.
   * Each layout is represented by a string of characters.
   */
  public enum Board {
    SUNDAY    ("0__________________0____0"),
    MONDAY    ("0___00___0_______________"),
    TUESDAY   ("000__0____0___0____0__000"),
    WEDNESDAY ("___00_______________00___"),
    THURSDAY  ("0___0_______________0___0"),
    FRIDAY    ("____0____0_____0____0____"),
    SATURDAY  ("000__0____0___0____0__000");

    /**
     * String representation of the layout, with '0' denoting blocks.
     */
    public final String day;

    Board(String day) {
      this.day = day;
    }
  }
}
