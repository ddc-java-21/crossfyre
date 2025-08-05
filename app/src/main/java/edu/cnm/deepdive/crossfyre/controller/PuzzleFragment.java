package edu.cnm.deepdive.crossfyre.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.crossfyre.R;
import edu.cnm.deepdive.crossfyre.databinding.FragmentPuzzleBinding;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzleDto.Guess;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzleDto.Guess.GuessPosition;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzleDto.Puzzle;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzleDto.Puzzle.PuzzleWord;
import edu.cnm.deepdive.crossfyre.view.adapter.SquareAdapter;
import edu.cnm.deepdive.crossfyre.viewmodel.PuzzleViewModel;
import javax.inject.Inject;

@AndroidEntryPoint
public class PuzzleFragment extends Fragment {

  private FragmentPuzzleBinding binding;
  private PuzzleViewModel viewModel;
  private PuzzleWord storedWord = null;
  private Puzzle storedPuzzle = null;

  //When an instance of the puzzle fragemnt gets created, right after it gets initialized and constructor is done
  // Hilt will inject an adapter
  @Inject
  SquareAdapter squareAdapter;

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

  private void sendGuess(char guessChar, int row, int column) {
    Guess guess = new Guess();
    GuessPosition position = new GuessPosition();
    position.setRow(row);
    position.setColumn(column);
    guess.setGuessPosition(position);
    guess.setGuess(guessChar);
    viewModel.sendGuess(guess);
  }

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
        binding.clueFullDescriptionText.setText(String.format(getString(R.string.clue_format_string), word.getClue()));
        binding.clueDirection.setText(word.getDirection().toString());

        if (word.getDirection() == PuzzleWord.Direction.ACROSS) {
          binding.directionToggleButton.setImageResource(R.drawable.arrow_across_24px);
          binding.directionToggleButton.setContentDescription(getString(R.string.puzzle_word_direction_across));
        } else {
          binding.directionToggleButton.setImageResource(R.drawable.arrow_down_24px);
          binding.directionToggleButton.setContentDescription(getString(R.string.puzzle_word_direction_down));
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
      if (position != null) {
        squareAdapter.setSelectedPosition(position);
      }
    });

    viewModel.getGuesses().observe(getViewLifecycleOwner(), (guesses) -> {
      if (guesses != null) {
        squareAdapter.setGuesses(guesses);
      }
    });

    // Add toggle button click listener
    binding.directionToggleButton.setOnClickListener((v) -> toggleClueDirection());
  }

  private void toggleClueDirection() {
    if (storedWord == null || storedPuzzle == null) {
      return;
    }

    PuzzleWord.Direction newDirection =
        (storedWord.getDirection() == PuzzleWord.Direction.ACROSS)
            ? PuzzleWord.Direction.DOWN
            : PuzzleWord.Direction.ACROSS;

    Integer currentPosition = viewModel.getSelectedSquare().getValue();

    int row = currentPosition / storedPuzzle.getSize();
    int col = currentPosition % storedPuzzle.getSize();

    // Look for a word at the current cell with the new direction
    for (PuzzleWord word : storedPuzzle.getPuzzleWords()) {
      if (word.getDirection() == newDirection && word.includes(row, col)) {
        // Directly select the new word while keeping the same square highlighted
        viewModel.selectWord(word);
        return;
      }
    }

    Log.d("PuzzleFragment", "No alternate direction word found.");
  }

  private void setCellWordStartNumber(PuzzleWord word) {
    int position = word.getWordPosition().getRow() * storedPuzzle.getSize() + word.getWordPosition().getColumn();
    int cellWordStartNumber = -1;
    for (int i = 0; i < storedPuzzle.getWordStarts().length; i++) {
      if (position == storedPuzzle.getWordStarts()[i]) {
        cellWordStartNumber = i + 1;
      }
    }

    binding.cellWordStartNumber.setText(String.valueOf(cellWordStartNumber));
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }
}
