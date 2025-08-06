package edu.cnm.deepdive.crossfyre.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.crossfyre.R;
import edu.cnm.deepdive.crossfyre.viewmodel.LoginViewModel;

/**
 * Pre-login fragment that serves as the initial screen for unauthenticated users.
 * This fragment automatically checks for existing authentication credentials and
 * navigates appropriately based on the authentication state.
 *
 * <p>Acts as a silent authentication check screen that redirects users to either
 * the main application (if authenticated) or the login screen (if authentication
 * fails or no valid credentials exist). Uses Hilt for dependency injection and
 * observes authentication state through the LoginViewModel.</p>
 */
@AndroidEntryPoint
public class PreLoginFragment extends Fragment {

  /**
   * Root view of the fragment layout, used for navigation operations.
   */
  private View root;

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
    root = inflater.inflate(R.layout.fragment_pre_login, container, false);
    return root;
  }

  /**
   * Called after the fragment's view has been created. Sets up observers for
   * authentication state monitoring and initiates the authentication refresh process.
   *
   * <p>Observes account changes to navigate to the main list screen upon successful
   * authentication, and monitors refresh errors to redirect to the login screen
   * when authentication fails or credentials are invalid.</p>
   *
   * @param view The view returned by onCreateView
   * @param savedInstanceState Bundle containing the activity's previously saved state
   */
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    LoginViewModel viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
    LifecycleOwner owner = getViewLifecycleOwner();
    viewModel
        .getAccount()
        .observe(owner, (account) -> {
          if (account != null) {
            Navigation.findNavController(root)
                .navigate(PreLoginFragmentDirections.showList());
          }
        });
    viewModel
        .getRefreshThrowable()
        .observe(owner, (throwable) -> {
          if (throwable != null) {
            Navigation.findNavController(root)
                .navigate(PreLoginFragmentDirections.showLogin());
          }
        });
    viewModel.refresh();
  }

  /**
   * Called when the view hierarchy associated with the fragment is being removed.
   * Cleans up the root view reference to prevent memory leaks.
   */
  @Override
  public void onDestroyView() {
    root = null;
    super.onDestroyView();
  }

}