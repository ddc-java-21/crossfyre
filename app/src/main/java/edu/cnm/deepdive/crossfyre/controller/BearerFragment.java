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
import edu.cnm.deepdive.crossfyre.model.dto.GuessDto;
import edu.cnm.deepdive.crossfyre.model.dto.GuessDto.GuessPosition;
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
//    viewModel
//        .getAccount()
//        .observe(owner, this::handleAccount);
//    binding.bearerToken.setText(account.getIdToken());
    viewModel
        .getAccount()
        .observe(owner, (ac) -> binding.bearerToken.setText(ac.getIdToken()));

    puzzleViewModel = new ViewModelProvider(activity).get(PuzzleViewModel.class);
    puzzleViewModel
        .getCurrentUserPuzzle()
        .observe(owner, (userPuzzle) -> binding.bearerToken.setText(userPuzzle != null
            ? userPuzzle.getPuzzle().getPuzzleWords().get(0).getClue()
            : ""
        ));


    // TEST GUESS CODE (INCOMPLETE)
//    GuessDto guess = new GuessDto();
//    GuessDto.GuessPosition guessPosition = new GuessPosition();
//    guess.setGuess('z');
//    guessPosition.setRow(2);
//    guessPosition.setColumn(2);
//    guess.setGuessPosition(guessPosition);
//    puzzleViewModel.sendGuess(guess);
//    puzzleViewModel
//        .getCurrentUserPuzzle()
//        .observe(owner, (userPuzzle) -> binding.bearerToken.setText(userPuzzle != null
//            ? userPuzzle.getGuesses().get(userPuzzle.getGuesses().size() - 1).getGuess().toString()
//            : ""
//        ));
  }

//  private void handleAccount (GoogleSignInAccount account) {
//  }
//
//  private void handleUserPuzzle (UserPuzzleDto userPuzzle) {
//
//  }
}

