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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.net.URL;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Represents a user profile in the Crossfyre application.
 * Stores authentication info, display details, and creation timestamp.
 */
@SuppressWarnings({"JpaDataSourceORMInspection", "RedundantSuppression"})
@Entity
@Table(name = "user_profile")
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"key", "displayName", "avatar", "created"})
public class User {

  /**
   * Maximum allowed length for display name.
   */
  private static final int MAX_DISPLAY_NAME_LENGTH = 20;

  /**
   * Maximum allowed length for OAuth key.
   */
  private static final int MAX_OAUTH_KEY_LENGTH = 30;

  /**
   * Internal database ID for the user (not exposed externally).
   */
  @Id
  @GeneratedValue
  @Column(name = "user_profile_id", nullable = false, updatable = false)
  @JsonIgnore
  private long id;

  /**
   * External UUID key used to identify the user publicly.
   */
  @Column(nullable = false, updatable = false, unique = true)
  @JsonProperty(value = "key", access = Access.READ_ONLY)
  private UUID externalKey;

  /**
   * OAuth key for user authentication, unique and not exposed externally.
   */
  @Column(nullable = false, updatable = false, length = MAX_OAUTH_KEY_LENGTH, unique = true)
  @JsonIgnore
  private String oauthKey;

  /**
   * Display name for the user, modifiable and with a maximum length.
   */
  @Column(nullable = false, updatable = true, length = MAX_DISPLAY_NAME_LENGTH)
  private String displayName;

  /**
   * Optional URL of the user's avatar image.
   */
  @Column(nullable = true, updatable = true)
  private URL avatar;

  /**
   * Timestamp of when the user profile was created (auto-generated).
   */
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  @JsonProperty(access = Access.READ_ONLY)
  private Instant created;

  /**
   * Gets the internal database ID of the user.
   *
   * @return the internal user ID.
   */
  public long getId() {
    return id;
  }

  /**
   * Gets the external UUID key identifying the user.
   *
   * @return the external UUID key.
   */
  public UUID getExternalKey() {
    return externalKey;
  }

  /**
   * Gets the OAuth key for authentication.
   *
   * @return the OAuth key string.
   */
  public String getOauthKey() {
    return oauthKey;
  }

  /**
   * Sets the OAuth key for the user.
   *
   * @param oauthKey the OAuth key to set.
   */
  public void setOauthKey(String oauthKey) {
    this.oauthKey = oauthKey;
  }

  /**
   * Gets the user's display name.
   *
   * @return the display name.
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Sets the user's display name.
   *
   * @param displayName the display name to set.
   */
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Gets the URL of the user's avatar image.
   *
   * @return the avatar URL.
   */
  public URL getAvatar() {
    return avatar;
  }

  /**
   * Sets the URL of the user's avatar image.
   *
   * @param avatar the avatar URL to set.
   */
  public void setAvatar(URL avatar) {
    this.avatar = avatar;
  }

  /**
   * Gets the timestamp when this user profile was created.
   *
   * @return creation timestamp.
   */
  public Instant getCreated() {
    return created;
  }

  /**
   * Computes a hash code based on the internal user ID.
   *
   * @return hash code.
   */
  @Override
  public int hashCode() {
    return Long.hashCode(id);
  }

  /**
   * Compares this User to another object for equality based on ID.
   *
   * @param obj the object to compare to.
   * @return true if equal, false otherwise.
   */
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

  /**
   * Lifecycle method to generate the external UUID key before persisting.
   */
  @PrePersist
  void generateFieldValues() {
    externalKey = UUID.randomUUID();
  }
}
