package edu.cnm.deepdive.crossfyre.hilt;

import android.content.Context;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import edu.cnm.deepdive.crossfyre.R;
import javax.inject.Singleton;
import okhttp3.logging.HttpLoggingInterceptor.Level;

@Module
@InstallIn(SingletonComponent.class)
public class PicassoModule {

  @Provides
  @Singleton
  Picasso providePicasso(@ApplicationContext Context context) {
    return new Picasso.Builder(context)
        .loggingEnabled(!context.getString(R.string.log_level).equalsIgnoreCase(Level.NONE.name()))
        .build();
  }

}
