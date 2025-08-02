package edu.cnm.deepdive.crossfyre.hilt;

import com.google.gson.JsonDeserializer;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import edu.cnm.deepdive.crossfyre.view.serialization.InstantDeserializer;
import java.time.Instant;

@Module
@InstallIn(SingletonComponent.class)
public interface DeserializerModule {

  @Binds
  JsonDeserializer<Instant> bindInstantDeserializer(InstantDeserializer deserializer);

}
