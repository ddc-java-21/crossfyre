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
import java.time.LocalDate;
import javax.inject.Singleton;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Hilt dependency injection module that provides network-related dependencies for the
 * CrossFyre application. This module configures and provides singleton instances of
 * Gson serialization, HTTP interceptors, and the main service proxy for API communication.
 *
 * <p>Installed in the SingletonComponent to ensure application-wide availability of
 * network dependencies. Configures custom serializers for Java 8 time types and
 * sets up HTTP logging based on application configuration.</p>
 */
@Module
@InstallIn(SingletonComponent.class)
public class CrossfyreServiceProxyModule {

  /**
   * Provides a configured Gson instance for JSON serialization and deserialization.
   * Configures custom type adapters for Instant and LocalDate types, and excludes
   * fields without @Expose annotations from serialization.
   *
   * @param instantDeserializer Custom deserializer for Instant objects
   * @param instantSerializer Custom serializer for Instant objects
   * @param localDateDeserializer Custom deserializer for LocalDate objects
   * @param localDateSerializer Custom serializer for LocalDate objects
   * @return Configured Gson instance with custom type adapters
   */
  @Provides
  @Singleton
  Gson provideGson(/*JsonDeserializer<Instant> deserializer*/ InstantDeserializer instantDeserializer, InstantSerializer instantSerializer,
      LocalDateDeserializer localDateDeserializer, LocalDateSerializer localDateSerializer) {
    return new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
//        .registerTypeAdapter(Instant.class, deserializer)
        .registerTypeAdapter(Instant.class, instantDeserializer)
        .registerTypeAdapter(Instant.class, instantSerializer)
        .registerTypeAdapter(LocalDate.class, localDateDeserializer)
        .registerTypeAdapter(LocalDate.class, localDateSerializer)
        .create();
  }

  /**
   * Provides an HTTP logging interceptor configured with the logging level specified
   * in the application's string resources. The logging level is read from the
   * log_level string resource and converted to uppercase for HttpLoggingInterceptor.
   *
   * @param context Application context for accessing string resources
   * @return Configured HttpLoggingInterceptor with appropriate logging level
   */
  @Provides
  @Singleton
  Interceptor provideInterceptor(@ApplicationContext Context context) {
    return new HttpLoggingInterceptor()
        .setLevel(Level.valueOf(context.getString(R.string.log_level).toUpperCase()));
  }

  /**
   * Provides the main service proxy for API communication with the CrossFyre backend.
   * Configures a complete Retrofit instance with OkHttpClient, Gson converter,
   * RxJava3 call adapter, and the base URL from application resources.
   *
   * @param context Application context for accessing string resources (base URL)
   * @param gson Configured Gson instance for JSON conversion
   * @param interceptor HTTP logging interceptor for request/response logging
   * @return Configured CrossfyreServiceProxy for API communication
   */
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