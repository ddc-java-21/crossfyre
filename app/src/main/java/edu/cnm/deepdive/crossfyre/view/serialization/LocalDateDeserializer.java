package edu.cnm.deepdive.crossfyre.view.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Gson deserializer for {@link LocalDate} objects, parsing ISO-8601 formatted date strings into
 * {@link LocalDate} instances.
 */
@Singleton
public class LocalDateDeserializer implements JsonDeserializer<LocalDate> {

  /**
   * Default constructor for dependency injection.
   */
  @Inject
  LocalDateDeserializer() {
  }

  /**
   * Deserializes a JSON element into a {@link LocalDate} by parsing the string representation.
   *
   * @param jsonElement The JSON element to deserialize.
   * @param type The type of the object to deserialize to (ignored).
   * @param jsonDeserializationContext Context for deserialization (ignored).
   * @return The parsed {@link LocalDate} object.
   * @throws JsonParseException If the string cannot be parsed to a {@link LocalDate}.
   */
  @Override
  public LocalDate deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    return LocalDate.parse(jsonElement.getAsString());
  }

}
