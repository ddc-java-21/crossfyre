package edu.cnm.deepdive.crossfyre.view.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import javax.inject.Inject;

public class LocalDateSerializer implements JsonSerializer<LocalDate> {

  @Inject
  LocalDateSerializer() {
  }

  @Override
  public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(src.toString()); // Converts Instant to an ISO 8601 string and wraps it in a JsonPrimitive.
  }

}
