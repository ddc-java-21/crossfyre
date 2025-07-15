package edu.cnm.deepdive.crossfyre.model.dto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.Expose;
import java.time.Instant;
import java.util.UUID;

public class Word {

  private static final String TO_STRING_FORMAT =
      "%1$s{key=%2$s, author=%3$s, text=%4$s, posted=%5$s}";

  @Expose(serialize = false)
  private final UUID key = null;

  @Expose(serialize = false)
  private final User author = null;

  @Expose(serialize = false)
  private final Instant posted = null;

  @Expose
  private String text;

  public UUID getKey() {
    return key;
  }

  public User getAuthor() {
    return author;
  }

  public Instant getPosted() {
    return posted;
  }

  public String getText() {
    return text;
  }

  public Word setText(String text) {
    this.text = text;
    return this;
  }

  @Override
  public int hashCode() {
    //noinspection DataFlowIssue
    return key.hashCode();
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    boolean comparison;
    if(this==obj) {
      comparison = true;
    } else if (obj instanceof Word other) {
      //noinspection ConstantValue,DataFlowIssue
      comparison = this.key.equals(other.key);
    } else {
      comparison = false;
    }
    return comparison;
  }

  @NonNull
  @Override
  public String toString() {
    return String.format(TO_STRING_FORMAT,
        getClass().getSimpleName(), key, author, text, posted);
  }
}
