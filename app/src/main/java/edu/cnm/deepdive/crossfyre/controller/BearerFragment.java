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
import edu.cnm.deepdive.crossfyre.databinding.FragmentMainBinding;
import edu.cnm.deepdive.crossfyre.model.dto.GuessDto;
import edu.cnm.deepdive.crossfyre.model.dto.GuessDto.GuessPosition;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzleDto;
import edu.cnm.deepdive.crossfyre.viewmodel.LoginViewModel;
import edu.cnm.deepdive.crossfyre.viewmodel.PuzzleViewModel;

@AndroidEntryPoint
public class BearerFragment extends Fragment {

  private FragmentBearerBinding binding;
  private LoginViewModel viewModel;

  private PuzzleViewModel puzzleViewModel;

  private boolean sentGuess = false;

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

    // ORIGINAL BEARER TOKEN DISPLAY CODE
//    viewModel
//        .getAccount()
//        .observe(owner, this::handleAccount);
//    binding.bearerToken.setText(account.getIdToken());

    // REVISED (TEST) BEARER TOKEN DISPLAY CODE
    viewModel
        .getAccount()
        .observe(owner, (ac) -> binding.bearerToken.setText(ac.getIdToken()));

    puzzleViewModel = new ViewModelProvider(activity).get(PuzzleViewModel.class);

    // DISPLAY DATA RETURNED FROM USERPUZZLE CODE
//    puzzleViewModel
//        .getCurrentUserPuzzle()
//        .observe(owner, (userPuzzle) -> binding.bearerToken.setText(userPuzzle != null
//            ? userPuzzle.getPuzzle().getPuzzleWords().get(0).getClue()
//            : ""
//        ));


    // TEST GUESS CODE (INCOMPLETE)
    GuessDto guess = new GuessDto();
    GuessDto.GuessPosition guessPosition = new GuessPosition();
    guess.setGuess('w');
    guessPosition.setRow(2);
    guessPosition.setColumn(2);
    guess.setGuessPosition(guessPosition);

    puzzleViewModel
        .getCurrentUserPuzzle()
        .observe(owner, (userPuzzle) -> {
          if (!sentGuess) {
            puzzleViewModel.sendGuess(guess); // cannot call this in here w/out a flag
            sentGuess = true;
          }
          binding.bearerToken.setText(userPuzzle != null
            ? !userPuzzle.getGuesses().isEmpty() && userPuzzle.getGuesses().get(userPuzzle.getGuesses().size() - 1) != null && userPuzzle.getGuesses().get(userPuzzle.getGuesses().size() - 1).getGuess() != null
              ? userPuzzle.getGuesses().get(userPuzzle.getGuesses().size() - 1).getGuess().toString()
              : ""
            : "");
            }
        );

  }

//  private void handleAccount (GoogleSignInAccount account) {
//  }
//
//  private void handleUserPuzzle (UserPuzzleDto userPuzzle) {
//
//  }
}

