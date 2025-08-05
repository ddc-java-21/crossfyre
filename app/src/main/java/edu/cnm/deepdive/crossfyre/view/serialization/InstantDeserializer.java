package edu.cnm.deepdive.crossfyre.view.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.time.Instant;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class InstantDeserializer implements JsonDeserializer<Instant> {

  @Inject
  InstantDeserializer() {
  }

  @Override
  public Instant deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    return Instant.parse(jsonElement.getAsString());
  }

}
