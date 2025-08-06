package edu.cnm.deepdive.crossfyre.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.google.android.material.snackbar.Snackbar;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.crossfyre.R;
import edu.cnm.deepdive.crossfyre.databinding.FragmentLoginBinding;
import edu.cnm.deepdive.crossfyre.viewmodel.LoginViewModel;

/**
 * Fragment responsible for handling user authentication and sign-in functionality.
 * This fragment provides the login interface and manages the Google Sign-In process,
 * including navigation to the main application screens upon successful authentication.
 *
 * <p>Uses the Activity Result API to handle sign-in results and observes authentication
 * state through the LoginViewModel. Navigates to the list screen upon successful login
 * and displays error messages for failed sign-in attempts.</p>
 */
@AndroidEntryPoint
public class LoginFragment extends Fragment {

  /**
   * View binding instance for accessing UI components in the login fragment layout.
   */
  private FragmentLoginBinding binding;

  /**
   * ViewModel for managing login state, authentication operations, and sign-in results.
   */
  private LoginViewModel viewModel;

  /**
   * Activity result launcher for handling Google Sign-In intent results using the
   * modern Activity Result API.
   */
  private ActivityResultLauncher<Intent> launcher;

  /**
   * Creates and returns the view hierarchy associated with the fragment.
   * Sets up the sign-in button click listener to initiate the authentication process.
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
    binding = FragmentLoginBinding.inflate(inflater, container, false);
    binding.signIn.setOnClickListener((v) -> viewModel.startSignIn(launcher));
    return binding.getRoot();
  }

  /**
   * Called after the fragment's view has been created. Initializes the ViewModel,
   * sets up observers for authentication state and error handling, and registers
   * the activity result launcher for sign-in operations.
   *
   * <p>Observes account changes to navigate to the list screen upon successful login,
   * and monitors sign-in errors to display appropriate user feedback.</p>
   *
   * @param view The view returned by onCreateView
   * @param savedInstanceState Bundle containing the activity's previously saved state
   */
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
    LifecycleOwner owner = getViewLifecycleOwner();
    viewModel
        .getAccount()
        .observe(owner, (account) -> {
          if (account != null) {
            Navigation.findNavController(binding.getRoot())
                .navigate(LoginFragmentDirections.showList());
          }
        });
    viewModel
        .getSignInThrowable()
        .observe(owner, (throwable) -> {
          if (throwable != null) {
            Snackbar.make(binding.getRoot(), R.string.sign_in_failure_message, Snackbar.LENGTH_LONG)
                .show();
          }
        });
    launcher = registerForActivityResult(new StartActivityForResult(), viewModel::completeSignIn);
  }

  /**
   * Called when the view hierarchy associated with the fragment is being removed.
   * Cleans up the view binding reference to prevent memory leaks.
   */
  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

}