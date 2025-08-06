package edu.cnm.deepdive.crossfyre.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle.State;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import edu.cnm.deepdive.crossfyre.R;
import edu.cnm.deepdive.crossfyre.databinding.FragmentMainBinding;
import edu.cnm.deepdive.crossfyre.viewmodel.LoginViewModel;

/**
 * Main application fragment that serves as the primary navigation hub after user authentication.
 * This fragment provides access to core application features including game play, user account
 * management, and navigation to other sections of the app.
 *
 * <p>Implements MenuProvider to handle options menu creation and item selection. Monitors
 * authentication state and redirects to login screens when the user is not authenticated.
 * Provides buttons for starting gameplay and signing out of the application.</p>
 */
public class MainFragment extends Fragment implements MenuProvider {

  /**
   * View binding instance for accessing UI components in the main fragment layout.
   */
  private FragmentMainBinding binding;

  /**
   * ViewModel for managing login state and authentication operations.
   */
  private LoginViewModel viewModel;

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
    binding = FragmentMainBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  /**
   * Called after the fragment's view has been created. Initializes the ViewModel,
   * sets up authentication state observers, configures menu provider, and establishes
   * click listeners for navigation buttons.
   *
   * <p>Observes account changes to handle sign-out scenarios and sets up navigation
   * to puzzle gameplay and sign-out functionality.</p>
   *
   * @param view The view returned by onCreateView
   * @param savedInstanceState Bundle containing the activity's previously saved state
   */
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    FragmentActivity activity = requireActivity();
    viewModel = new ViewModelProvider(activity).get(LoginViewModel.class);
    LifecycleOwner owner = getViewLifecycleOwner();
    viewModel
        .getAccount()
        .observe(owner, this::handleAccount);
    activity.addMenuProvider(this, owner, State.RESUMED);
    binding.playButton.setOnClickListener(v ->
        Navigation.findNavController(v)
            .navigate(R.id.action_main_fragment_to_puzzle_fragment)
    );
    binding.signOutButton.setOnClickListener(v -> {
          viewModel.signOut();
          Navigation.findNavController(v)
              .navigate(R.id.show_pre_login);
        }
    );
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

  /**
   * Creates the options menu for this fragment by inflating the main options menu resource.
   *
   * @param menu The menu object to be populated
   * @param menuInflater The MenuInflater to use for inflating menu resources
   */
  @Override
  public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
    menuInflater.inflate(R.menu.main_options, menu);
  }

  /**
   * Handles selection of menu items from the options menu. Provides navigation
   * to different sections of the app and handles sign-out functionality.
   *
   * @param menuItem The selected menu item
   * @return true if the menu item selection was handled, false otherwise
   */
  @Override
  public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
    boolean handled = false;
    if (menuItem.getItemId() == R.id.sign_out) {
      viewModel.signOut();
      handled = true;
    } else if (menuItem.getItemId() == R.id.puzzle_menu) {
      Navigation.findNavController(requireView())
          .navigate(R.id.action_main_fragment_to_puzzle_fragment);
      handled = true;
    } else {
      Navigation.findNavController(requireView())
          .navigate(R.id.show_bearer_fragment);
    }
    return handled;
  }

  /**
   * Handles changes in user authentication state. Navigates to the pre-login screen
   * when the user account becomes null (indicating sign-out or authentication failure).
   *
   * @param account The current GoogleSignInAccount, or null if user is not authenticated
   * @noinspection deprecation
   */
  private void handleAccount(GoogleSignInAccount account) {
    if (account == null) {
      Navigation.findNavController(binding.getRoot())
          .navigate(MainFragmentDirections.showPreLogin());
    }
  }
}