package edu.cnm.deepdive.crossfyre.view.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.Instant;
import javax.inject.Inject;

/**
 * Gson serializer for {@link Instant} objects, converting them into ISO-8601 formatted
 * JSON strings.
 */
public class InstantSerializer implements JsonSerializer<Instant> {

  /**
   * Default constructor for dependency injection.
   */
  @Inject
  InstantSerializer() {
  }

  /**
   * Serializes an {@link Instant} into a JSON primitive string.
   *
   * @param src The {@link Instant} to serialize.
   * @param typeOfSrc The actual type (ignored).
   * @param context The context of serialization (ignored).
   * @return A {@link JsonPrimitive} containing the ISO-8601 string representation of the {@link Instant}.
   */
  @Override
  public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(src.toString()); // Converts Instant to ISO-8601 string.
  }

}
