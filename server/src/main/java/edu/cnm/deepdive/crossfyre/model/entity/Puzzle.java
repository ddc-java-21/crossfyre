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
@JsonPropertyOrder({"key", "title", "size", "board", "created"})
public class Puzzle {

  public static final int PUZZLE_TITLE_LENGTH = 15;
  @Id
  @GeneratedValue
  @Column(name = "puzzle_id", nullable = false, updatable = false)
  @JsonIgnore
  private long id;

  @Column(nullable = false, updatable = false, unique = true)
  @JsonProperty(value = "key", access = Access.READ_ONLY)
  private UUID externalKey;

  @Column(nullable = false, updatable = true, unique = true, length = PUZZLE_TITLE_LENGTH)
  private String title;


  @Column(nullable = false, updatable = false)
  @JsonProperty(access = Access.READ_ONLY)
  private int size;

  @Column(nullable = false, updatable = false)
  private String board;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  @JsonProperty(value = "created", access = Access.READ_ONLY)
  private Instant created;

  @OneToMany(mappedBy = "puzzle", fetch = FetchType.LAZY) // TN 2025-07-07 removed Cascade.ALL and orphanRemoval for Puzzle --> UserPuzzle relationship
  @JsonIgnore
  private final List<UserPuzzle> userPuzzles = new LinkedList<>();

  @OneToMany(mappedBy = "puzzle", fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,orphanRemoval = true)
  @JsonIgnore
  private final List<PuzzleWord> puzzleWords = new LinkedList<>(); // TN 2025-07-07 added puzzleWords list


  public long getId() {
    return id;
  }

  public UUID getExternalKey() {
    return externalKey;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public String getBoard() {
    return board;
  }

  public void setBoard(String board) {
    this.board = board;
  }

  public Instant getCreated() {
    return created;
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
}
