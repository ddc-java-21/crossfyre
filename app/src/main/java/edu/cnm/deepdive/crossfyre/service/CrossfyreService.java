package edu.cnm.deepdive.crossfyre.service;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import dagger.hilt.android.qualifiers.ApplicationContext;
import edu.cnm.deepdive.crossfyre.model.dto.User;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzleDto;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzleDto.Guess;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.time.Instant;
import javax.inject.Inject;
import javax.inject.Singleton;

/** @noinspection deprecation*/
@Singleton
public class CrossfyreService {

  private static final String BEARER_TOKEN_FORMAT = "Bearer %s";

  private final GoogleSignInService signInService;
  private final CrossfyreServiceProxy proxy;
  private final SharedPreferences preferences;

  private final Scheduler scheduler;


  @Inject
  CrossfyreService(@ApplicationContext Context context,
      @NonNull GoogleSignInService signInService,
      @NonNull CrossfyreServiceProxy proxy) {
    this.signInService = signInService;
    this.proxy = proxy;
    preferences = PreferenceManager.getDefaultSharedPreferences(context);
    scheduler = Schedulers.single();
  }

  public Single<User> getMyProfile() {
    return getBearerToken()
        .flatMap(proxy::getMyProfile);
  }

  public Single<UserPuzzleDto> getUserPuzzle(Instant date) {
    return getBearerToken()
        .flatMap((token) -> proxy.getUserPuzzle(token, date));
  }

  public Single<UserPuzzleDto> sendGuess(Instant date, Guess guess) {
    return getBearerToken()
        .flatMap((token) -> proxy.sendGuess(token, date, guess));
  }

  private Single<String> getBearerToken() {
    //noinspection ReactiveStreamsNullableInLambdaInTransform
    return signInService
        .refresh()
        .map(GoogleSignInAccount::getIdToken)
        .map((token) -> String.format(BEARER_TOKEN_FORMAT, token))
        .observeOn(scheduler);
  }

}
