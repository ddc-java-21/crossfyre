package edu.cnm.deepdive.crossfyre.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import java.util.UUID;

public class Word {

  @Id
  @GeneratedValue
  @Column(name = "word_id", nullable = false, updatable = false)
  @JsonIgnore
  private long id;

  @Column(nullable = false, updatable = false, unique = true)
  @JsonProperty(value = "key", access = Access.READ_ONLY)
  private UUID externalKey;

  @Column(nullable = false, updatable = true, unique = true, length = 30)
  private String spelling;
  
  @Column(nullable = false, updatable = true, length = 30)
  private Record wordClue;
  
  private int[] position = new int[2];

  // TODO: 7/2/2025 decide if puzzle handles direction assignment and if it's an enum 
//  private Direction direction;

  public long getId() {
    return id;
  }

  public UUID getExternalKey() {
    return externalKey;
  }

  public String getSpelling() {
    return spelling;
  }

  public void setSpelling(String spelling) {
    this.spelling = spelling;
  }

  public int[] getPosition() {
    return position;
  }

  public Word setPosition(int[] position) {
    this.position = position;
    return this;
  }
  
  @Override
  public int hashCode() {
    return Long.hashCode(id);
  }
//  @Override
//  public boolean equals(Object obj) {
//    boolean comparison;
//    if (this == obj) {
//      comparison = true;
//    } else if (obj instanceof Guess other) {
//      comparison = (this.id != 0 && this.id == other);
//    } else {
//      comparison = false;
//    }

//    return comparison;

//  }

  @PrePersist
  void generateFieldValues() {
    externalKey = UUID.randomUUID();
  }
}
