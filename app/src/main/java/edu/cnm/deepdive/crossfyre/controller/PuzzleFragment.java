package edu.cnm.deepdive.crossfyre.controller;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.snackbar.Snackbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.crossfyre.R;
import edu.cnm.deepdive.crossfyre.databinding.FragmentPuzzleBinding;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzleDto.Guess;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzleDto.Guess.GuessPosition;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzleDto.Puzzle;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzleDto.Puzzle.PuzzleWord;
import edu.cnm.deepdive.crossfyre.view.adapter.SquareAdapter;
import edu.cnm.deepdive.crossfyre.viewmodel.PuzzleViewModel;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import nl.dionsegijn.konfetti.core.Position.Relative;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Shape.Circle;
import nl.dionsegijn.konfetti.core.models.Shape.Square;
import nl.dionsegijn.konfetti.xml.KonfettiView;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import java.util.concurrent.TimeUnit;
import nl.dionsegijn.konfetti.core.PartyFactory;


/**
 * Fragment responsible for displaying and managing the crossword puzzle game interface. This
 * fragment handles the interactive crossword grid, clue display, user input, and communication with
 * the puzzle game logic through the PuzzleViewModel.
 *
 * <p>Manages the puzzle grid using a custom SquareAdapter, observes puzzle state
 * changes, handles user interactions for selecting squares and entering guesses, and displays clue
 * information with directional toggle functionality. Uses Hilt for dependency injection of the grid
 * adapter.</p>
 */
@AndroidEntryPoint
public class PuzzleFragment extends Fragment {

  /**
   * View binding instance for accessing UI components in the puzzle fragment layout.
   */
  private FragmentPuzzleBinding binding;

  /**
   * ViewModel for managing puzzle state, user interactions, and game logic.
   */
  private PuzzleViewModel viewModel;

  /**
   * Currently selected word in the puzzle, cached for UI updates and clue display.
   */
  private PuzzleWord storedWord = null;

  /**
   * Current puzzle data, cached for grid operations and word numbering calculations.
   */
  private Puzzle storedPuzzle = null;

  /**
   * Position of the currently selected square in the puzzle grid.
   */
  private Integer currentPosition = null;


  /**
   * Current boolean check to see if the puzzle is solved to show Toast dialog
   */
  private boolean hasShownSolvedDialog = false;


  /**
   * Custom adapter for managing the puzzle grid display and user interactions. Injected by Hilt
   * when the fragment instance is created.
   */
  @Inject
  SquareAdapter squareAdapter;

  /**
   * Creates and returns the view hierarchy associated with the fragment. Sets up the puzzle grid
   * click listener and configures the square adapter with a guess submission callback.
   *
   * @param inflater           The LayoutInflater object that can be used to inflate views
   * @param container          The parent view that the fragment's UI will be attached to
   * @param savedInstanceState Bundle containing the activity's previously saved state
   * @return The root view of the inflated fragment layout
   */
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentPuzzleBinding.inflate(inflater, container, false);
    // need to implement when the user clicks on an item we need to know what item is clicked
    // we can attach a listener to a gridview and know when an item is selected in a gridview
    binding.puzzleGrid.setOnItemClickListener(
        (parent, view, position, id) -> viewModel.selectSquare(position));

    squareAdapter.setListener(this::sendGuess);
    return binding.getRoot();
  }

  /**
   * Sends a user's guess to the ViewModel for processing and validation. Creates a Guess object
   * with the character input and grid position coordinates.
   *
   * @param guessChar The character guessed by the user
   * @param row       The row position in the puzzle grid
   * @param column    The column position in the puzzle grid
   */
  private void sendGuess(char guessChar, int row, int column) {
    Guess guess = new Guess();
    GuessPosition position = new GuessPosition();
    position.setRow(row);
    position.setColumn(column);
    guess.setGuessPosition(position);
    guess.setGuess(guessChar);
    viewModel.sendGuess(guess);
  }

  /**
   * Called after the fragment's view has been created. Initializes the ViewModel, configures the
   * puzzle grid adapter, and sets up observers for all puzzle state changes including grid updates,
   * word selections, and user guesses.
   *
   * <p>Establishes observers for grid data, word start positions, selected cell positions,
   * selected word details, current puzzle state, selected square position, and user guesses. Also
   * configures the direction toggle button click listener.</p>
   *
   * @param view               The view returned by onCreateView
   * @param savedInstanceState Bundle containing the activity's previously saved state
   */
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    Log.d("PuzzleFragment", "onViewCreated");
    super.onViewCreated(view, savedInstanceState);

    viewModel = new ViewModelProvider(requireActivity()).get(PuzzleViewModel.class);

    binding.puzzleGrid.setAdapter(squareAdapter);

    // Observe board data from ViewModel
    viewModel.getGrid().observe(getViewLifecycleOwner(), (grid) -> {
//      Character[][] currentBoard = board;
      if (grid != null) {
        squareAdapter.setGrid(grid);
        binding.puzzleGrid.setNumColumns(grid.length);
        squareAdapter.notifyDataSetChanged();
      }
    });

    // Observe word start positions for clue numbering
    viewModel.getWordStarts().observe(getViewLifecycleOwner(), (wordStarts) -> {
      if (wordStarts != null) {
        squareAdapter.setWordStarts(wordStarts);
      }
    });

    // Observe highlighted positions
    viewModel.getSelectedCellPositions().observe(getViewLifecycleOwner(), (positions) -> {
      if (positions != null) {
        squareAdapter.setHighlightedPositions(positions);
      }
    });

    // Observe selected word details
    viewModel.getSelectedWord().observe(getViewLifecycleOwner(), (word) -> {
      if (word != null) {
        storedWord = word;
        binding.clueFullDescriptionText.setText(
            String.format(getString(R.string.clue_format_string), word.getClue()));
        binding.clueDirection.setText(word.getDirection().toString());

        // ⬇️ ICON + CONTENT DESCRIPTION LOGIC ⬇️
        if (word.getDirection() == PuzzleWord.Direction.ACROSS) {
          binding.directionToggleButton.setImageResource(R.drawable.arrow_across_24px);
          binding.directionToggleButton.setContentDescription(
              getString(R.string.puzzle_word_direction_across));
        } else {
          binding.directionToggleButton.setImageResource(R.drawable.arrow_down_24px);
          binding.directionToggleButton.setContentDescription(
              getString(R.string.puzzle_word_direction_down));
        }

        if (storedPuzzle != null) {
          // method does here
          setCellWordStartNumber(storedWord);
        }
      }
    });

    viewModel.getCurrentPuzzle().observe(getViewLifecycleOwner(), (puzzle) -> {
      if (puzzle != null) {
        storedPuzzle = puzzle;
        if (storedWord != null) {
          // method goes here
          setCellWordStartNumber(storedWord);
        }
      }
    });

    viewModel.getSelectedSquare().observe(getViewLifecycleOwner(), (position) -> {
      currentPosition = position;
      if (position != null) {
        squareAdapter.setSelectedPosition(position);
      }
    });

    viewModel.getGuesses().observe(getViewLifecycleOwner(), (guesses) -> {
      if (guesses != null) {
        squareAdapter.setGuesses(guesses);
      }
    });

    viewModel.getSolved().observe(getViewLifecycleOwner(), (solved) -> {
      if (Boolean.TRUE.equals(solved)) {

//        KonfettiView konfettiView = binding.konfettiView;
//        konfettiView.setVisibility(View.VISIBLE);
//
//        EmitterConfig emitterConfig = new Emitter(100, TimeUnit.MILLISECONDS).perSecond(50);
//
//        Party party = new PartyFactory(emitterConfig)
//            .spread(360)
//                .shapes(Square.INSTANCE, Circle.INSTANCE)
//                    .position(new Relative(0.5,0.0))
//                        .build();
//
//        konfettiView.start(party);

        new AlertDialog.Builder(requireContext())
            .setTitle("🎉 Puzzle Solved!")
            .setMessage("Congratulations! You completed the puzzle.")
//            .setCancelable(false)
            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
//            .setPositiveButton("Go Back", (dialog, which) -> {
//                  konfettiView.stopGracefully();
//                  konfettiView.setVisibility(View.GONE);
//                  navigateToMain();
//                })
                .show();

//        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//          if (isAdded()) {
//            konfettiView.stopGracefully();
//            konfettiView.setVisibility(View.GONE);
//            navigateToMain();
//          }
//        }, 3000);



//        Snackbar.make(binding.getRoot(), R.string.solved_string, Snackbar.LENGTH_SHORT).show();
      }
    });

    // Add toggle button click listener
    binding.directionToggleButton.setOnClickListener((v) -> toggleClueDirection());

  }

  /**
   * Handles the navigation after showDialog has been flagged to true to get the user to the
   * fragment_main from the fragment_puzzle
   */
  private void navigateToMain() {
    // Using Navigation Component
    NavController navController = NavHostFragment.findNavController(this);
    navController.navigate(R.id.main_fragment);
  }


  /**
   * Handles the direction toggle button click to switch between across and down word selections for
   * the current square position. Re-triggers square selection to cycle through available word
   * directions.
   */
  private void toggleClueDirection() {
    if (currentPosition == null) {
      return;
    }

    viewModel.selectSquare(currentPosition); // Triggers observers
    return;

//    Log.d("PuzzleFragment", "No alternate direction word found.");
  }

  /**
   * Calculates and displays the clue number for the selected word based on its starting position in
   * the puzzle grid. Uses the puzzle's word starts array to determine the appropriate numbering
   * sequence.
   *
   * @param word The puzzle word for which to calculate and display the clue number
   */
  private void setCellWordStartNumber(PuzzleWord word) {
    int position = word.getWordPosition().getRow() * storedPuzzle.getSize() + word.getWordPosition()
        .getColumn();
    int cellWordStartNumber = -1;
    for (int i = 0; i < storedPuzzle.getWordStarts().length; i++) {
      if (position == storedPuzzle.getWordStarts()[i]) {
        cellWordStartNumber = i + 1;
      }
    }

    binding.cellWordStartNumber.setText(String.valueOf(cellWordStartNumber));
  }

  /**
   * Called when the view hierarchy associated with the fragment is being removed. Cleans up the
   * view binding reference to prevent memory leaks.
   */
  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }
}