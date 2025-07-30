package edu.cnm.deepdive.crossfyre.controller;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.crossfyre.databinding.FragmentPuzzleBinding;
import edu.cnm.deepdive.crossfyre.model.dto.PuzzleWord;
import edu.cnm.deepdive.crossfyre.view.adapter.SquareAdapter;
import edu.cnm.deepdive.crossfyre.viewmodel.PuzzleViewModel;
import java.util.List;
import javax.inject.Inject;

@AndroidEntryPoint
public class PuzzleFragment extends Fragment {

  private FragmentPuzzleBinding binding;
  private PuzzleViewModel viewModel;
  private SquareAdapter adapter;

  @Inject
  public SquareAdapter squareAdapter; // Injected via Hilt

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = FragmentPuzzleBinding.inflate(getLayoutInflater());

    // TODO: 7/30/25 Finish adding on create
//    binding.clueDirection.listen
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentPuzzleBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity()).get(PuzzleViewModel.class);

    // Observe ViewModel state
    viewModel.getSelectedWord().observe(getViewLifecycleOwner(), word -> {
      if (word != null) {
        binding.clueText.setText(word.getClue());
      }
    });

    viewModel.getSelectedDirection().observe(getViewLifecycleOwner(), dir -> {
      binding.clueDirection.setText(dir.name());
    });

    viewModel.getWords().observe(getViewLifecycleOwner(), words -> {
      populateGrid(words);
    });
  }

  private void populateGrid(List<PuzzleWord> words) {
    GridLayout grid = binding.puzzleGrid;
    grid.removeAllViews();
    grid.setRowCount(15);
    grid.setColumnCount(15);

    for (PuzzleWord word : words) {
      PuzzleWord.WordPosition pos = word.wordPosition;
      for (int i = 0; i < pos.getWordLength(); i++) {
        int row = word.getWordDirection() == PuzzleWord.Direction.ACROSS ? pos.getWordRow() : pos.getWordRow() + i;
        int col = word.getWordDirection() == PuzzleWord.Direction.ACROSS ? pos.getWordColumn() + i : pos.getWordColumn();

        TextView cell = new TextView(requireContext());
        cell.setText(" ");
        cell.setPadding(8, 8, 8, 8);
        cell.setGravity(Gravity.CENTER);

        GridLayout.LayoutParams params = new GridLayout.LayoutParams(
            GridLayout.spec(row), GridLayout.spec(col));
        params.width = 0;
        params.height = 0;
        params.columnSpec = GridLayout.spec(col, 1f);
        params.rowSpec = GridLayout.spec(row, 1f);
        cell.setLayoutParams(params);

        cell.setOnClickListener(v -> viewModel.selectWord(word));

        cell.setOnLongClickListener(v -> {
          viewModel.toggleDirection();
          return true;
        });

        grid.addView(cell);
      }
    }
  }
}
