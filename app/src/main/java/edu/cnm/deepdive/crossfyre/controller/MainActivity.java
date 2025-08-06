package edu.cnm.deepdive.crossfyre.controller;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.crossfyre.R;
import edu.cnm.deepdive.crossfyre.databinding.ActivityMainBinding;

/**
 * Main activity serving as the primary entry point and navigation host for the CrossFyre application.
 * This activity manages the app's navigation architecture, handles edge-to-edge display configuration,
 * and coordinates between different fragments using the Navigation Component.
 *
 * <p>Implements edge-to-edge display support with proper window insets handling and configures
 * the action bar with navigation support. Uses Hilt for dependency injection throughout the
 * application lifecycle.</p>
 */
@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

  /**
   * Tag used for logging purposes, derived from the class name.
   */
  private static final String TAG = MainActivity.class.getSimpleName();

  /**
   * View binding instance for accessing UI components in the main activity layout.
   */
  private ActivityMainBinding binding;

  /**
   * Configuration object that defines the app bar behavior and top-level destinations
   * for navigation.
   */
  private AppBarConfiguration appBarConfig;

  /**
   * Navigation controller responsible for managing fragment navigation and transitions.
   */
  private NavController navController;

  /**
   * Called when the activity is starting. Initializes the UI components and sets up
   * the navigation architecture for the application.
   *
   * @param savedInstanceState Bundle containing the activity's previously saved state,
   *                          or null if the activity is being created for the first time
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupUI();
    setupNavigation();
  }

  /**
   * Handles the "Up" navigation action in the action bar. Delegates to NavigationUI
   * to perform the appropriate navigation based on the current fragment and app bar configuration.
   *
   * @return true if navigation was handled successfully, false otherwise
   */
  @Override
  public boolean onSupportNavigateUp() {
    return NavigationUI.navigateUp(navController, appBarConfig);
  }

  /**
   * Configures the user interface components including edge-to-edge display,
   * window insets handling, and view binding setup.
   */
  private void setupUI() {
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    EdgeToEdge.enable(this);
    ViewCompat.setOnApplyWindowInsetsListener(binding.navHostFragment, MainActivity::adjustInsets);
    setContentView(binding.getRoot());
  }

  /**
   * Initializes the navigation architecture including the action bar, app bar configuration,
   * and navigation controller. Defines the top-level destinations and connects the action bar
   * with the navigation component.
   */
  private void setupNavigation() {
    setSupportActionBar(binding.toolbar);
    appBarConfig = new AppBarConfiguration.Builder(
        R.id.pre_login_fragment, R.id.login_fragment, R.id.main_fragment)
        .build();
    NavHostFragment host = binding.navHostFragment.getFragment();
    navController = host.getNavController();
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
  }

  /**
   * Adjusts the view margins to accommodate system bars (status bar, navigation bar)
   * when using edge-to-edge display. This ensures content is not obscured by system UI.
   *
   * @param view The view whose margins need to be adjusted
   * @param windowInsets The current window insets containing system bar information
   * @return WindowInsetsCompat.CONSUMED to indicate that the insets have been handled
   */
  @NonNull
  private static WindowInsetsCompat adjustInsets(
      @NonNull View view, @NonNull WindowInsetsCompat windowInsets) {
    Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
    MarginLayoutParams mlp = (MarginLayoutParams) view.getLayoutParams();
    mlp.leftMargin = insets.left;
    mlp.bottomMargin = insets.bottom;
    mlp.rightMargin = insets.right;
    view.setLayoutParams(mlp);
    return WindowInsetsCompat.CONSUMED;
  }

}