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
import jakarta.persistence.OneToOne;
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
@JsonPropertyOrder({"key", "title", "created", "size", "board"})
public class UserPuzzle {

  @Id
  @GeneratedValue
  @Column(name = "user_puzzle_id", nullable = false, updatable = false)
  @JsonIgnore
  private long id;

  @Column(nullable = false, updatable = false, unique = true)
  @JsonProperty(value = "key", access = Access.READ_ONLY)
  private UUID externalKey;

  @Column(nullable = false, updatable = true, unique = true, length = 30)
  private String title;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  @JsonProperty(access = Access.READ_ONLY)
  private Instant created;

  @OneToMany(mappedBy = "userPuzzle", fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,orphanRemoval = true)
  @JsonIgnore
  private final List<UserWord> userWords = new LinkedList<>();

  @Column(nullable = false, updatable = false)
  @JsonProperty(access = Access.READ_ONLY)
  private int size;

  @Column(nullable = false, updatable = true)
  private String board;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "solution_puzzle_id", nullable = false, updatable = false)
  @JsonProperty(value = "solution", access = Access.READ_ONLY)
  @JsonIgnore
  private SolutionPuzzle solutionPuzzle;

  @OneToOne(mappedBy = "userPuzzle",
      cascade = CascadeType.ALL,orphanRemoval = true)
  @JsonIgnore
  private Game game;

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

  public SolutionPuzzle getSolutionPuzzle() {
    return solutionPuzzle;
  }

  public void setSolutionPuzzle (SolutionPuzzle solutionPuzzle) {
    this.solutionPuzzle = solutionPuzzle;
  }

  public Instant getCreated() {
    return created;
  }

  public List<UserWord> getUserWords() {
    return userWords;
  }

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
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
    } else if (obj instanceof UserPuzzle other) {
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
