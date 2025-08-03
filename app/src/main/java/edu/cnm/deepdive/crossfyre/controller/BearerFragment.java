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
import edu.cnm.deepdive.crossfyre.databinding.FragmentBearerBinding;
import edu.cnm.deepdive.crossfyre.databinding.FragmentMainBinding;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzleDto;
import edu.cnm.deepdive.crossfyre.viewmodel.LoginViewModel;
import edu.cnm.deepdive.crossfyre.viewmodel.PuzzleViewModel;

public class BearerFragment extends Fragment {

  private FragmentBearerBinding binding;
  private LoginViewModel viewModel;

  private PuzzleViewModel puzzleViewModel;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentBearerBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    FragmentActivity activity = requireActivity();
    viewModel = new ViewModelProvider(activity).get(LoginViewModel.class);
    GoogleSignInAccount account = viewModel.getAccount().getValue();
    LifecycleOwner owner = getViewLifecycleOwner();
    viewModel
        .getAccount()
        .observe(owner, this::handleAccount);
    binding.bearerToken.setText(account.getIdToken());

//    puzzleViewModel = new ViewModelProvider(activity).get(PuzzleViewModel.class);
//    UserPuzzleDto userPuzzle = puzzleViewModel.getUserPuzzle().getValue();
//    binding.bearerToken.setText(userPuzzle != null ? userPuzzle.getPuzzle().getPuzzleWords().get(0).getClue() : "");
  }

  private void handleAccount (GoogleSignInAccount account) {


  }
}

