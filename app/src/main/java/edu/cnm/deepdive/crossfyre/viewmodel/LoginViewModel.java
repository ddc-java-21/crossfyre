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

  @Inject
  LoginViewModel(GoogleSignInService service) {
    this.service = service;
    account = new MutableLiveData<>();
    refreshThrowable = new MutableLiveData<>();
    signInThrowable = new MutableLiveData<>();
    pending = new CompositeDisposable();
  }

  public LiveData<GoogleSignInAccount> getAccount() {
    return account;
  }

  public LiveData<Throwable> getRefreshThrowable() {
    return refreshThrowable;
  }

  public LiveData<Throwable> getSignInThrowable() {
    return signInThrowable;
  }

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

  public void startSignIn(@NonNull ActivityResultLauncher<Intent> launcher) {
    refreshThrowable.setValue(null);
    signInThrowable.setValue(null);
    service.startSignIn(launcher);
  }

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

  private void postThrowable(
      @NonNull Throwable throwable, @NonNull MutableLiveData<Throwable> target) {
    Log.e(TAG, throwable.getMessage(), throwable);
    target.postValue(throwable);
  }

}
