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
// import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
// import org.hibernate.validator.constraints.Length;

@SuppressWarnings({"JpaDataSourceORMInspection", "RedundantSuppression"})
@Entity
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"key", "author", "text", "posted"})
public class Guess {

  private static final int MAX_MESSAGE_LENGTH = 255; // TODO: 7/6/2025 change to puzzle grid size

  @Id
  @GeneratedValue
  @Column(name = "message_id", nullable = false, updatable = false)
  @JsonIgnore
  private long id;

  @Column(nullable = false, updatable = false, unique = true)
  @JsonProperty(value = "key", access = Access.READ_ONLY)
  private UUID externalKey;

  // TODO: 7/2/2025 Are guess objects limited here or is that handled by the viewModel?
//  @NotBlank
//  @Length(max = MAX_MESSAGE_LENGTH)
//  @Column(nullable = false, updatable = false, length = MAX_MESSAGE_LENGTH)
  private String text;

  // TODO: 7/2/2025 Do we get the completion time stamp from here or when the comparison resolves
//  @CreationTimestamp
//  @Temporal(TemporalType.TIMESTAMP)
//  @Column(nullable = false, updatable = false)
//  @JsonProperty(access = Access.READ_ONLY)
//  private Instant posted;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "player_id", nullable = false, updatable = false)
  @JsonProperty(access = Access.READ_ONLY)
  private User author;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "puzzle_id", nullable = false, updatable = false)
  @JsonIgnore
  private Puzzle puzzle;

  public long getId() {
    return id;
  }

  public UUID getExternalKey() {
    return externalKey;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

//  public Instant getPosted() {
//    return posted;
//  }

  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  public Puzzle getPuzzle() {
    return puzzle;
  }

  public void setChannel(Puzzle puzzle) {
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
    } else if (obj instanceof Guess other) {
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
