package edu.cnm.deepdive.crossfyre.hilt;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import edu.cnm.deepdive.crossfyre.R;
import edu.cnm.deepdive.crossfyre.service.CrossfyreServiceProxy;
import edu.cnm.deepdive.crossfyre.view.serialization.InstantDeserializer;
import edu.cnm.deepdive.crossfyre.view.serialization.InstantSerializer;
import edu.cnm.deepdive.crossfyre.view.serialization.LocalDateDeserializer;
import edu.cnm.deepdive.crossfyre.view.serialization.LocalDateSerializer;
import java.time.Instant;
import javax.inject.Singleton;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class CrossfyreServiceProxyModule {


  @Provides
  @Singleton
  Gson provideGson(/*JsonDeserializer<Instant> deserializer*/ InstantDeserializer instantDeserializer, InstantSerializer instantSerializer,
      LocalDateDeserializer localDateDeserializer, LocalDateSerializer localDateSerializer) {
    return new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
//        .registerTypeAdapter(Instant.class, deserializer)
        .registerTypeAdapter(Instant.class, instantDeserializer)
        .registerTypeAdapter(Instant.class, instantSerializer)
        .registerTypeAdapter(Instant.class, localDateDeserializer)
        .registerTypeAdapter(Instant.class, localDateSerializer)
        .create();
  }

  @Provides
  @Singleton
  Interceptor provideInterceptor(@ApplicationContext Context context) {
    return new HttpLoggingInterceptor()
        .setLevel(Level.valueOf(context.getString(R.string.log_level).toUpperCase()));
  }

  @Provides
  @Singleton
  CrossfyreServiceProxy provideProxy(
      @ApplicationContext Context context, Gson gson, Interceptor interceptor) {
    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build();
    return new Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .baseUrl(context.getString(R.string.base_url))
        .build()
        .create(CrossfyreServiceProxy.class);
  }

}
