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
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.UpdateTimestamp;

@SuppressWarnings({"JpaDataSourceORMInspection", "RedundantSuppression"})
@Entity
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"key", "user", "user-puzzle", "solved"})
public class Game {

  private static final int MAX_MESSAGE_LENGTH = 255;

  @Id
  @GeneratedValue
  @Column(name = "game_id", nullable = false, updatable = false)
  @JsonIgnore
  private long id;

  @Column(nullable = false, updatable = false, unique = true)
  @JsonProperty(value = "key", access = Access.READ_ONLY)
  private UUID externalKey;

  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false)
  private Instant solvedTime;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_profile_id", nullable = false, updatable = false)
  @JsonProperty(value = "user", access = Access.READ_ONLY)
  private User player;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_puzzle_id", nullable = false, updatable = false)
  @JsonProperty(value = "puzzle", access = Access.READ_ONLY)
  private UserPuzzle userPuzzle;

  public long getId() {
    return id;
  }

  public UUID getExternalKey() {
    return externalKey;
  }

  public Instant getSolvedTime() {
    return solvedTime;
  }

  public void setSolvedTime(Instant completed) {
    this.solvedTime = completed;
  }

  public User getPlayer() {
    return player;
  }

  public void setPlayer(User player) {
    this.player = player;
  }

  public UserPuzzle getUserPuzzle() {
    return userPuzzle;
  }

  public void setUserPuzzle(UserPuzzle userPuzzle) {
    this.userPuzzle = userPuzzle;
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
    } else if (obj instanceof Game other) {
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
