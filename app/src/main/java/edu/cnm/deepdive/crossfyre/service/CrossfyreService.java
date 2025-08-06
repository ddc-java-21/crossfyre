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
import java.time.LocalDate;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Provides access to remote puzzle and user services via authenticated HTTP calls.
 * <p>
 * This service wraps the {@link CrossfyreServiceProxy} with token management provided by
 * {@link GoogleSignInService}, and operates primarily using RxJava {@link Single} for asynchronous
 * processing on a dedicated scheduler.
 * </p>
 */
@Singleton
public class CrossfyreService {

  private static final String BEARER_TOKEN_FORMAT = "Bearer %s";

  private final GoogleSignInService signInService;
  private final CrossfyreServiceProxy proxy;
  private final SharedPreferences preferences;
  private final Scheduler scheduler;

  /**
   * Constructs an instance of {@code CrossfyreService}, injecting the app context,
   * sign-in service, and proxy.
   *
   * @param context       application context, used to retrieve preferences.
   * @param signInService service to manage Google Sign-In.
   * @param proxy         proxy interface to backend REST services.
   */
  @Inject
  CrossfyreService(@ApplicationContext Context context,
      @NonNull GoogleSignInService signInService,
      @NonNull CrossfyreServiceProxy proxy) {
    this.signInService = signInService;
    this.proxy = proxy;
    preferences = PreferenceManager.getDefaultSharedPreferences(context);
    scheduler = Schedulers.single();
  }

  /**
   * Retrieves the current user's profile from the backend service.
   *
   * @return a {@link Single} emitting the {@link User} object on success, or an error.
   */
  public Single<User> getMyProfile() {
    return getBearerToken()
        .flatMap(proxy::getMyProfile);
  }

  /**
   * Fetches the {@link UserPuzzleDto} for the specified date.
   *
   * @param date the date for which to fetch the puzzle.
   * @return a {@link Single} emitting the puzzle data or an error.
   */
  public Single<UserPuzzleDto> getUserPuzzle(LocalDate date) {
    return getBearerToken()
        .flatMap((token) -> proxy.getUserPuzzle(token, date));
  }

  /**
   * Sends a guess to the backend service for the specified puzzle date.
   *
   * @param date  the date of the puzzle being guessed.
   * @param guess the guess to submit.
   * @return a {@link Single} emitting the updated {@link UserPuzzleDto} or an error.
   */
  public Single<UserPuzzleDto> sendGuess(LocalDate date, Guess guess) {
    return getBearerToken()
        .flatMap((token) -> proxy.sendGuess(token, date, guess));
  }

  /**
   * Retrieves a bearer token for authorization by refreshing the current Google Sign-In account,
   * then formatting the token for use in HTTP Authorization headers.
   *
   * @return a {@link Single} emitting a formatted bearer token.
   */
  private Single<String> getBearerToken() {
    //noinspection ReactiveStreamsNullableInLambdaInTransform
    return signInService
        .refresh()
        .map(GoogleSignInAccount::getIdToken)
        .map((token) -> String.format(BEARER_TOKEN_FORMAT, token))
        .observeOn(scheduler);
  }

}
