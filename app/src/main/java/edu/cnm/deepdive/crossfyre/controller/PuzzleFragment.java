package edu.cnm.deepdive.crossfyre.controller;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.crossfyre.R;
import edu.cnm.deepdive.crossfyre.databinding.FragmentPuzzleBinding;
import edu.cnm.deepdive.crossfyre.model.dto.PuzzleWord;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzle.Guess.Puzzle;
import edu.cnm.deepdive.crossfyre.view.adapter.SquareAdapter;
import edu.cnm.deepdive.crossfyre.viewmodel.PuzzleViewModel;
import java.util.List;
import javax.inject.Inject;

@AndroidEntryPoint
public class PuzzleFragment extends Fragment {

  private FragmentPuzzleBinding binding;
  private PuzzleViewModel viewModel;

  //When an instance of the puzzle fragemnt gets created, right after it gets initialized and constructor is done
  // Hilt will inject an adapter
  @Inject
  SquareAdapter squareAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = FragmentPuzzleBinding.inflate(getLayoutInflater());
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentPuzzleBinding.inflate(inflater, container, false);
    // need to implement when the user clicks on an item we need to know what item is clicked
    // we can attach a listener to a gridview and know when an item is selected in a gridview
    binding.puzzleGrid.setOnItemSelectedListener(new OnItemSelectedListener() {
      // tells you which position in the gridview like 1,2,3,...25, the view is a gridview
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // TODO: 8/1/25 Use position to determine which row and column are clicked and use that to update the viewmodel

      }

      // Usually do nothing in this method
      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
      }
    });
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    viewModel = new ViewModelProvider(requireActivity()).get(PuzzleViewModel.class);

    binding.puzzleGrid.setAdapter(squareAdapter);

    // Set click listener for grid items
    binding.puzzleGrid.setOnItemClickListener((parent, view1, position, id) -> {
      viewModel.selectSquare(position);
    });

    // Observe ViewModel state
    viewModel.getSelectedWord().observe(getViewLifecycleOwner(), word -> {
      if (word != null) {
        binding.clueText.setText(word.getClue());

        // Added binding to get the clue for the word in the lambda
       binding.clueDirection.setText(word.getDirection().toString());
      }

    });

    // this is where were updating the livedata
    viewModel.getWords().observe(getViewLifecycleOwner(), words -> {
      // TODO: 8/1/25 Pass words to the adapter of the clues which is another sub adapter of ArrayAdapter
      // TODO: Need an ArrayAdapter of puzzleword so that you can get the clue but split the Across and Downs into different lists and that will go into a ListViews
      // Viewmodel should know which clue to put in live data by knowing what the user clicked and if the user clicked on
      // the same grid twice it should update livedata and display the correct clue accordingly
      // Could use a snackbar, could be more simple since it doesn't have to be part of the layout
    });
    // TODO: 8/1/25 Observe LiveData containing the board
    // TODO: 8/1/25 Use the board to set the number of columns in the gridview which tells teh gridview how to flow horizontally and when wrap
    // TODO: 8/1/25 Pass board to adapter.setBoard()
  }

//  private void initializeTextFields() {
//    binding.clueDirection.setText();
//  }

}
