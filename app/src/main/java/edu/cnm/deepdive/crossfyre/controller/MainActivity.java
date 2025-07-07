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

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  private ActivityMainBinding binding;
  private AppBarConfiguration appBarConfig;
  private NavController navController;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupUI();
    setupNavigation();
  }

  @Override
  public boolean onSupportNavigateUp() {
    return NavigationUI.navigateUp(navController, appBarConfig);
  }

  private void setupUI() {
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    EdgeToEdge.enable(this);
    ViewCompat.setOnApplyWindowInsetsListener(binding.navHostFragment, MainActivity::adjustInsets);
    setContentView(binding.getRoot());
  }

  private void setupNavigation() {
    setSupportActionBar(binding.toolbar);
    appBarConfig = new AppBarConfiguration.Builder(
        R.id.pre_login_fragment, R.id.login_fragment, R.id.main_fragment)
        .build();
    NavHostFragment host = binding.navHostFragment.getFragment();
    navController = host.getNavController();
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
  }

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