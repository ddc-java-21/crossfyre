package edu.cnm.deepdive.crossfyre.model.dto;

import com.google.gson.annotations.Expose;
import java.net.URL;
import java.time.Instant;
import java.util.UUID;

public class User {

  private static final String TO_STRING_FORMAT =
      "%1$s{key=%2$s, displayName=%3$s, avatar=%4$s, created=%5$s}";

  @Expose(serialize = false)
  private final UUID key = null;

  @Expose(serialize = false)
  private final Instant created = null;

  @Expose
  private String displayName;

  @Expose
  private URL avatar;

  public UUID getKey() {
    return key;
  }

  public Instant getCreated() {
    return created;
  }

  public String getDisplayName() {
    return displayName;
  }

  public User setDisplayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  public URL getAvatar() {
    return avatar;
  }

  public User setAvatar(URL avatar) {
    this.avatar = avatar;
    return this;
  }

}
