package edu.cnm.deepdive.crossfyre.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.crossfyre.databinding.FragmentBearerBinding;
import edu.cnm.deepdive.crossfyre.viewmodel.LoginViewModel;
import edu.cnm.deepdive.crossfyre.viewmodel.PuzzleViewModel;

/**
 * Fragment responsible for displaying bearer token information to the user.
 * This fragment integrates with the authentication system to show the current
 * user's ID token from Google Sign-In.
 *
 * <p>Uses Hilt for dependency injection and observes authentication state
 * through the LoginViewModel.</p>
 */
@AndroidEntryPoint
public class BearerFragment extends Fragment {

  /**
   * View binding instance for accessing UI components in the fragment layout.
   */
  private FragmentBearerBinding binding;

  /**
   * ViewModel for managing login and authentication state.
   */
  private LoginViewModel viewModel;

  /**
   * ViewModel for managing puzzle-related data and operations.
   */
  private PuzzleViewModel puzzleViewModel;

  /**
   * Flag to track whether a guess has been sent to prevent duplicate submissions.
   */
  private boolean sentGuess = false;

  /**
   * Creates and returns the view hierarchy associated with the fragment.
   *
   * @param inflater The LayoutInflater object that can be used to inflate views
   * @param container The parent view that the fragment's UI will be attached to
   * @param savedInstanceState Bundle containing the activity's previously saved state
   * @return The root view of the inflated fragment layout, or null
   */
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentBearerBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  /**
   * Called after the fragment's view has been created. Sets up view models,
   * observes authentication state, and configures the bearer token display.
   *
   * @param view The view returned by onCreateView
   * @param savedInstanceState Bundle containing the activity's previously saved state
   */
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    FragmentActivity activity = requireActivity();
    viewModel = new ViewModelProvider(activity).get(LoginViewModel.class);
    GoogleSignInAccount account = viewModel.getAccount().getValue();
    LifecycleOwner owner = getViewLifecycleOwner();

    // ORIGINAL BEARER TOKEN DISPLAY CODE
//    viewModel
//        .getAccount()
//        .observe(owner, this::handleAccount);
//    binding.bearerToken.setText(account.getIdToken());

    // REVISED (TEST) BEARER TOKEN DISPLAY CODE
    viewModel
        .getAccount()
        .observe(owner, (ac) -> binding.bearerToken.setText(ac.getIdToken()));

  }

}