package edu.cnm.deepdive.crossfyre.model.dto;

import com.google.gson.annotations.Expose;
import java.net.URL;
import java.time.Instant;
import java.util.UUID;

/**
 * Represents a user in the system, including identifying information,
 * display name, avatar URL, and creation timestamp.
 *
 * <p>This class is used for data transfer (DTO) and is serialized/deserialized
 * with Gson. Some fields are marked with {@link Expose} annotations to control
 * their serialization behavior.</p>
 */
public class User {

  /**
   * Format string used by {@code toString()} methods (if implemented).
   */
  private static final String TO_STRING_FORMAT =
      "%1$s{key=%2$s, displayName=%3$s, avatar=%4$s, created=%5$s}";

  /**
   * Unique identifier for the user.
   * <p>This field is not serialized.</p>
   */
  @Expose(serialize = false)
  private final UUID key = null;

  /**
   * Timestamp indicating when the user was created.
   * <p>This field is not serialized.</p>
   */
  @Expose(serialize = false)
  private final Instant created = null;

  /**
   * Display name of the user.
   */
  @Expose
  private String displayName;

  /**
   * URL of the user's avatar image.
   */
  @Expose
  private URL avatar;

  /**
   * Returns the unique key (UUID) for this user.
   *
   * @return the user's UUID.
   */
  public UUID getKey() {
    return key;
  }

  /**
   * Returns the creation timestamp for this user.
   *
   * @return the timestamp the user was created.
   */
  public Instant getCreated() {
    return created;
  }

  /**
   * Returns the display name of the user.
   *
   * @return the user's display name.
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Sets the display name of the user.
   *
   * @param displayName the name to set.
   * @return this {@code User} instance for method chaining.
   */
  public User setDisplayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * Returns the avatar URL of the user.
   *
   * @return the avatar URL.
   */
  public URL getAvatar() {
    return avatar;
  }

  /**
   * Sets the avatar URL of the user.
   *
   * @param avatar the URL of the user's avatar.
   * @return this {@code User} instance for method chaining.
   */
  public User setAvatar(URL avatar) {
    this.avatar = avatar;
    return this;
  }

}
