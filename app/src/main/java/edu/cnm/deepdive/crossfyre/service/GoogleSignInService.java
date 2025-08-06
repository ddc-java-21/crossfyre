package edu.cnm.deepdive.crossfyre.service;

import android.content.Context;
import android.content.Intent;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import dagger.hilt.android.qualifiers.ApplicationContext;
import edu.cnm.deepdive.crossfyre.R;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Service that handles Google Sign-In authentication, refreshing tokens,
 * and signing users in or out.
 */
@Singleton
public class GoogleSignInService {

  private final GoogleSignInClient client;

  /**
   * Creates and configures a {@link GoogleSignInClient} with required scopes and ID token.
   *
   * @param context the application context used to configure the sign-in client
   */
  @Inject
  GoogleSignInService(@ApplicationContext Context context) {
    GoogleSignInOptions options = new GoogleSignInOptions.Builder()
        .requestEmail()
        .requestProfile()
        .requestId()
        .requestIdToken(context.getString(R.string.client_id))
        .build();
    client = GoogleSignIn.getClient(context, options);
  }

  /**
   * Attempts to silently refresh the current sign-in session.
   *
   * @return a {@link Single} emitting the refreshed {@link GoogleSignInAccount}
   */
  public Single<GoogleSignInAccount> refresh() {
    return Single.create((SingleEmitter<GoogleSignInAccount> emitter) ->
            client
                .silentSignIn()
                .addOnSuccessListener(emitter::onSuccess)
                .addOnFailureListener(emitter::onError)
        )
        .observeOn(Schedulers.io());
  }

  /**
   * Initiates the sign-in flow by launching the intent via a launcher.
   *
   * @param launcher launcher used to handle the result
   */
  public void startSignIn(@NonNull ActivityResultLauncher<Intent> launcher) {
    launcher.launch(client.getSignInIntent());
  }

  /**
   * Completes the sign-in process using the result from the launched intent.
   *
   * @param result the result of the sign-in activity
   * @return a {@link Single} emitting the signed-in {@link GoogleSignInAccount}
   */
  public Single<GoogleSignInAccount> completeSignIn(ActivityResult result) {
    return Single.create((SingleEmitter<GoogleSignInAccount> emitter) -> {
          try {
            GoogleSignInAccount account =
                GoogleSignIn.getSignedInAccountFromIntent(result.getData())
                    .getResult(ApiException.class);
            emitter.onSuccess(account);
          } catch (ApiException e) {
            emitter.onError(e);
          }
        })
        .subscribeOn(Schedulers.io());
  }

  /**
   * Signs out the currently authenticated Google account.
   *
   * @return a {@link Completable} that completes when the sign-out succeeds
   */
  public Completable signOut() {
    return Completable.create((emitter) ->
            client
                .signOut()
                .addOnSuccessListener((ignored) -> emitter.onComplete())
                .addOnFailureListener(emitter::onError)
        )
        .observeOn(Schedulers.io());
  }

}
