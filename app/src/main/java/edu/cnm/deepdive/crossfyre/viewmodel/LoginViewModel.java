package edu.cnm.deepdive.crossfyre.viewmodel;

import android.content.Intent;
import android.util.Log;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.cnm.deepdive.crossfyre.service.GoogleSignInService;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import javax.inject.Inject;

/**
 * ViewModel responsible for managing Google Sign-In authentication state and operations,
 * including sign-in, sign-out, and token refresh handling.
 *
 * <p>Uses {@link GoogleSignInService} for interacting with Google Sign-In APIs and exposes
 * authentication status and errors via {@link LiveData}.</p>
 *
 * @noinspection deprecation
 */
@HiltViewModel
public class LoginViewModel extends ViewModel implements DefaultLifecycleObserver {

  private static final String TAG = LoginViewModel.class.getSimpleName();

  private final GoogleSignInService service;
  private final MutableLiveData<GoogleSignInAccount> account;
  private final MutableLiveData<Throwable> refreshThrowable;
  private final MutableLiveData<Throwable> signInThrowable;
  private final CompositeDisposable pending;

  /**
   * Constructs the ViewModel with injected {@link GoogleSignInService}.
   *
   * @param service the Google Sign-In service to manage authentication.
   */
  @Inject
  LoginViewModel(GoogleSignInService service) {
    this.service = service;
    account = new MutableLiveData<>();
    refreshThrowable = new MutableLiveData<>();
    signInThrowable = new MutableLiveData<>();
    pending = new CompositeDisposable();
  }

  /**
   * Gets a {@link LiveData} holding the current signed-in Google account.
   *
   * @return the live data for GoogleSignInAccount.
   */
  public LiveData<GoogleSignInAccount> getAccount() {
    return account;
  }

  /**
   * Gets a {@link LiveData} holding any error thrown during token refresh operations.
   *
   * @return the live data for refresh-related errors.
   */
  public LiveData<Throwable> getRefreshThrowable() {
    return refreshThrowable;
  }

  /**
   * Gets a {@link LiveData} holding any error thrown during sign-in operations.
   *
   * @return the live data for sign-in related errors.
   */
  public LiveData<Throwable> getSignInThrowable() {
    return signInThrowable;
  }

  /**
   * Attempts to silently refresh the current Google sign-in session.
   * Clears prior errors before attempting refresh.
   */
  public void refresh() {
    refreshThrowable.setValue(null);
    signInThrowable.setValue(null);
    service
        .refresh()
        .subscribe(
            account::postValue,
            (throwable) -> postThrowable(throwable, refreshThrowable),
            pending
        );
  }

  /**
   * Starts the interactive sign-in flow using the given launcher.
   * Clears prior errors before starting sign-in.
   *
   * @param launcher the launcher to initiate the sign-in intent.
   */
  public void startSignIn(@NonNull ActivityResultLauncher<Intent> launcher) {
    refreshThrowable.setValue(null);
    signInThrowable.setValue(null);
    service.startSignIn(launcher);
  }

  /**
   * Completes the sign-in process after receiving the result.
   * Clears prior errors before processing.
   *
   * @param result the activity result from the sign-in flow.
   */
  public void completeSignIn(@NonNull ActivityResult result) {
    refreshThrowable.setValue(null);
    signInThrowable.setValue(null);
    service
        .completeSignIn(result)
        .subscribe(
            account::postValue,
            (throwable) -> postThrowable(throwable, signInThrowable),
            pending
        );
  }

  /**
   * Signs out the current user and clears the account data.
   * Clears prior errors before signing out.
   */
  public void signOut() {
    refreshThrowable.setValue(null);
    signInThrowable.setValue(null);
    Disposable disposable = service
        .signOut()
        .doFinally(() -> account.postValue(null))
        .doOnError((throwable) -> postThrowable(throwable, signInThrowable))
        .subscribe();
    pending.add(disposable);
  }

  /**
   * Helper method to log and post throwable errors to the specified target LiveData.
   *
   * @param throwable the error to post.
   * @param target the target LiveData to receive the error.
   */
  private void postThrowable(
      @NonNull Throwable throwable, @NonNull MutableLiveData<Throwable> target) {
    Log.e(TAG, throwable.getMessage(), throwable);
    target.postValue(throwable);
  }

}
