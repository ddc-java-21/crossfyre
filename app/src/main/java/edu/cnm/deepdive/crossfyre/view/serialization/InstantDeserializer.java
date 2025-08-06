package edu.cnm.deepdive.crossfyre.view.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.time.Instant;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Gson deserializer for {@link Instant} objects, parsing ISO-8601 formatted strings into
 * {@link Instant} instances.
 */
@Singleton
public class InstantDeserializer implements JsonDeserializer<Instant> {

  /**
   * Default constructor for dependency injection.
   */
  @Inject
  InstantDeserializer() {
  }

  /**
   * Deserializes a JSON element into an {@link Instant} by parsing the string representation.
   *
   * @param jsonElement The JSON element to deserialize.
   * @param type The type of the object to deserialize to (ignored).
   * @param jsonDeserializationContext Context for deserialization (ignored).
   * @return The parsed {@link Instant} object.
   * @throws JsonParseException If the string cannot be parsed to an {@link Instant}.
   */
  @Override
  public Instant deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    return Instant.parse(jsonElement.getAsString());
  }

}
