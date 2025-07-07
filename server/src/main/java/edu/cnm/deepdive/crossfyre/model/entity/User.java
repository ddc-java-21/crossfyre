package edu.cnm.deepdive.crossfyre.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
//import jakarta.validation.constraints.NotBlank;
import java.net.URL;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.validator.constraints.Length;

@SuppressWarnings({"JpaDataSourceORMInspection", "RedundantSuppression"})
@Entity
@Table(
    name = "user_profile"
)
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"key", "displayName", "avatar", "created"})
public class User {

  private static final int MAX_DISPLAY_NAME_LENGTH = 30;
  private static final int MAX_OAUTH_KEY_LENGTH = 30;

  @Id
  @GeneratedValue
  @Column(name = "user_profile_id", nullable = false, updatable = false)
  @JsonIgnore
  private long id;

  @Column(nullable = false, updatable = false, unique = true)
  @JsonProperty(value = "key", access = Access.READ_ONLY)
  private UUID externalKey;

  @Column(nullable = false, updatable = false, length = MAX_OAUTH_KEY_LENGTH, unique = true)
  @JsonIgnore
  private String oauthKey;

//  @NotBlank
//  @Length(max = MAX_DISPLAY_NAME_LENGTH)
    @Column(nullable = false, updatable = true, length = MAX_DISPLAY_NAME_LENGTH, unique = false)
    private String displayName;

  @Column(nullable = true, updatable = true)
  private URL avatar;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  @JsonProperty(access = Access.READ_ONLY)
  private Instant created;

  public long getId() {
    return id;
  }

  public UUID getExternalKey() {
    return externalKey;
  }

  public String getOauthKey() {
    return oauthKey;
  }

  public void setOauthKey(String oauthKey) {
    this.oauthKey = oauthKey;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public URL getAvatar() {
    return avatar;
  }

  public void setAvatar(URL avatar) {
    this.avatar = avatar;
  }

  public Instant getCreated() {
    return created;
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
    } else if (obj instanceof User other) {
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
