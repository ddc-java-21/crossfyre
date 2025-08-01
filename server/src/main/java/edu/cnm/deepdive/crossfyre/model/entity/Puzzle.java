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
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;

@SuppressWarnings({"JpaDataSourceORMInspection", "RedundantSuppression"})
@Entity
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"key", "size", "board", "created, date, puzzleWords"})
public class Puzzle {

  @Id
  @GeneratedValue
  @Column(name = "puzzle_id", nullable = false, updatable = false)
  @JsonIgnore
  private long id;

  @Column(nullable = false, updatable = false, unique = true)
  @JsonProperty(value = "key", access = Access.READ_ONLY)
  private UUID externalKey;

  @Column(nullable = false, updatable = false)
  @JsonProperty(access = Access.READ_ONLY)
  private int size;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, updatable = false)
  private Board board;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  @JsonProperty(value = "created", access = Access.READ_ONLY)
  private Instant created;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = true, updatable = true)
  @JsonProperty(value = "date", access = Access.READ_WRITE)
  private Instant date;

  @OneToMany(mappedBy = "puzzle", fetch = FetchType.LAZY) // TN 2025-07-07 removed Cascade.ALL and orphanRemoval for Puzzle --> UserPuzzle relationship
  @JsonIgnore
  private List<UserPuzzle> userPuzzles = new LinkedList<>();

  @OneToMany(mappedBy = "puzzle", fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,orphanRemoval = true)
  @JsonProperty(value = "puzzleWords", access = Access.READ_ONLY)
  private final List<PuzzleWord> puzzleWords = new LinkedList<>(); // TN 2025-07-07 added puzzleWords list


  public long getId() {
    return id;
  }

  public UUID getExternalKey() {
    return externalKey;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public Board getBoard() {
    return board;
  }

  public void setBoard(Board board) {
    this.board = board;
  }

  public Instant getCreated() {
    return created;
  }

  public void setCreated(Instant created) {
    this.created = created;
  }

  public Instant getDate() {
    return date;
  }

  public void setDate(Instant date) {
    this.date = date;
  }

  public List<UserPuzzle> getUserPuzzles() {
    return userPuzzles;
  }

  public List<PuzzleWord> getPuzzleWords() {
    return puzzleWords;
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
    } else if (obj instanceof Puzzle other) {
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

  public enum Board {
    SUNDAY    ("0__________________0____0"/*, 5*/),   //   \u0000 OR \000
    MONDAY    ("0___00___0_______________"),
    TUESDAY   ("00_____________________00"),
    WEDNESDAY ("___00_______________00___"),
    THURSDAY  ("0___0_______________0___0"),
    FRIDAY    ("____0____0_____0____0____"),
    SATURDAY  ("___00____0_____0____00___");


    public final String day; // private
    // field for row

    Board(String day/*, int size*/) {
      this.day = day;
      //this.size = size;
    }

    // getter/accessor

  }

}
