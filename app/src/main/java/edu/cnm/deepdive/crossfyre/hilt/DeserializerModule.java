package edu.cnm.deepdive.crossfyre.hilt;

import com.google.gson.JsonDeserializer;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import edu.cnm.deepdive.crossfyre.view.serialization.InstantDeserializer;
import java.time.Instant;

/**
 * Hilt module that provides a binding for a custom {@link JsonDeserializer} for {@link Instant}.
 * <p>
 * This module installs into the {@link SingletonComponent}, making the provided dependency
 * available application-wide.
 */
@Module
@InstallIn(SingletonComponent.class)
public interface DeserializerModule {

  /**
   * Binds a custom implementation of {@link JsonDeserializer} for {@link Instant} type
   * deserialization using {@link InstantDeserializer}.
   *
   * @param deserializer the custom deserializer to use for {@link Instant} values.
   * @return the {@link JsonDeserializer} for {@link Instant}.
   */
  @Binds
  JsonDeserializer<Instant> bindInstantDeserializer(InstantDeserializer deserializer);

}
