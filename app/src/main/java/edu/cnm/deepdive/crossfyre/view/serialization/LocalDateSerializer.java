package edu.cnm.deepdive.crossfyre.view.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.LocalDate;
import javax.inject.Inject;

/**
 * Gson serializer for {@link LocalDate} objects, converting them into ISO-8601 formatted
 * JSON strings.
 */
public class LocalDateSerializer implements JsonSerializer<LocalDate> {

  /**
   * Default constructor for dependency injection.
   */
  @Inject
  LocalDateSerializer() {
  }

  /**
   * Serializes a {@link LocalDate} into a JSON primitive string.
   *
   * @param src The {@link LocalDate} to serialize.
   * @param typeOfSrc The actual type (ignored).
   * @param context The context of serialization (ignored).
   * @return A {@link JsonPrimitive} containing the ISO-8601 string representation of the {@link LocalDate}.
   */
  @Override
  public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(src.toString()); // Converts LocalDate to ISO-8601 string.
  }

}
