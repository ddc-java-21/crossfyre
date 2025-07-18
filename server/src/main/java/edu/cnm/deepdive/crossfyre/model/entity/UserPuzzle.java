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
@JsonPropertyOrder({"key", "created", "solved"})
public class UserPuzzle {

  @Id
  @GeneratedValue
  @Column(name = "user_puzzle_id", nullable = false, updatable = false)
  @JsonIgnore
  private long id;

  @Column(nullable = false, updatable = false, unique = true)
  @JsonProperty(value = "key", access = Access.READ_ONLY)
  private UUID externalKey;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  @JsonProperty(value = "created", access = Access.READ_ONLY)
  private Instant created;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  @JsonProperty(value = "solved", access = Access.READ_ONLY)
  private Instant solved;

  @OneToMany(mappedBy =
      "userPuzzle", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
  @JsonIgnore
  private final List<Guess> guesses = new LinkedList<>();

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "puzzle_id", nullable = false, updatable = false)
  @JsonProperty(value = "puzzle", access = Access.READ_ONLY)
  @JsonIgnore
  private Puzzle puzzle;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false, updatable = false)
  @JsonProperty(value = "user", access = Access.READ_ONLY)
  @JsonIgnore
  private User user;

  public long getId() {
    return id;
  }

  public UUID getExternalKey() {
    return externalKey;
  }

  public Puzzle getSolutionPuzzle() {
    return puzzle;
  }

  public void setSolutionPuzzle (Puzzle puzzle) {
    this.puzzle = puzzle;
  }

  public Instant getCreated() {
    return created;
  }

  public List<Guess> getUserWords() {
    return guesses;
  }

  public User getUser() {
    return user;
  }

  public UserPuzzle setUser(User user) {
    this.user = user;
    return this;
  }

  public Instant getSolved() {
    return solved;
  }

  public UserPuzzle setSolved(Instant solved) {
    this.solved = solved;
    return this;
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
