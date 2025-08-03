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
import edu.cnm.deepdive.crossfyre.view.adapter.PuzzleClueAdapter;
import edu.cnm.deepdive.crossfyre.view.adapter.SquareAdapter;
import edu.cnm.deepdive.crossfyre.viewmodel.PuzzleViewModel;
import java.util.List;
import javax.inject.Inject;

@AndroidEntryPoint
public class PuzzleFragment extends Fragment {

  private FragmentPuzzleBinding binding;
  private PuzzleViewModel viewModel;

  //When an instance of the puzzle fragment gets created, right after it gets initialized and constructor is done
  // Hilt will inject an adapter
  @Inject
  SquareAdapter squareAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = FragmentPuzzleBinding.inflate(getLayoutInflater());
//    initializeTextFields();

    // TODO: 7/30/25 Finish adding on create
//    binding.clueDirection.listen
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
        viewModel.selectSquare(position);
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

    // Observe board data from ViewModel
    viewModel.getBoard().observe(getViewLifecycleOwner(), board -> {
      if (board != null) {
        squareAdapter.setBoard(board);
      }
    });

    // Observe clue numbering map from ViewModel
    viewModel.getWordStartMap().observe(getViewLifecycleOwner(), map -> {
      if (map != null) {
        squareAdapter.setWordStartMap(map);
      }
    });

    // Observe highlighted positions
    viewModel.getSelectedCellPosition().observe(getViewLifecycleOwner(), positions -> {
      if (positions != null) {
        squareAdapter.setHighlightedPositions(positions);
        squareAdapter.notifyDataSetChanged();
      }
    });

    // Click handling for grid cells
    binding.puzzleGrid.setOnItemClickListener((parent, view1, position, id) -> {
      viewModel.selectSquare(position);
    });

    // Observe selected word details
    viewModel.getSelectedWord().observe(getViewLifecycleOwner(), word -> {
      if (word != null) {
        binding.clueText.setText(word.getClue());
        // Added binding to get the clue for the word in the lambda
        binding.clueDirection.setText(word.getDirection().toString());
        binding.clueFullDescriptionText.setText(word.getClue());
        binding.clueDirection.setText(word.getDirection().toString());
      }
    });

    PuzzleClueAdapter clueAdapter = new PuzzleClueAdapter(requireContext());

    // this is where were updating the livedata
    viewModel.getWords().observe(getViewLifecycleOwner(), words -> {
      // TODO: 8/1/25 Pass words to the adapter of the clues which is another sub adapter of ArrayAdapter
      // TODO: Need an ArrayAdapter of puzzleword so that you can get the clue but split the Across and Downs
      //  into different lists and that will go into a ListViews
      if (words != null && !words.isEmpty()) {
        clueAdapter.setPuzzleWords(words);

        List<PuzzleWord> acrossClues = clueAdapter.getAcrossClues().getValue();
        List<PuzzleWord> downClues = clueAdapter.getDownClues().getValue();

        PuzzleWord firstClue = (acrossClues != null && !acrossClues.isEmpty())
            ? acrossClues.get(0)
            : (downClues != null && !downClues.isEmpty() ? downClues.get(0) : null);

        if (firstClue != null) {
          binding.clueText.setText(firstClue.getClue());
        }
        else {
          binding.clueText.setText("");
        }
      }
    });
//      viewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);
//      viewModel.setSelectedChannel(channel);
//      viewModel.getMessages().observe(getViewLifecycleOwner(), (messages) -> {
//        adapter.setMessages(messages);
//        binding.messages.scrollToPosition(messages.size() - 1);
//      });
//    }
    viewModel.getBoard().observe(getViewLifecycleOwner(), (board) -> {
      squareAdapter.setBoard(board);
      binding.puzzleGrid.setNumColumns(board[0].length); // informs GridView's layout
    });


    // TODO: 8/1/25 Use the board to set the number of columns in the gridview which tells teh gridview how to flow horizontally and when wrap
    // TODO: 8/1/25 Pass board to adapter.setBoard()
  }

//  private void initializeTextFields() {
//    binding.clueDirection.setText();
//  }

}
