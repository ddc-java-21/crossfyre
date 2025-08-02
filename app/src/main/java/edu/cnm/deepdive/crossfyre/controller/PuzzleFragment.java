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
  @Inject
  SquareAdapter adapter;

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
    binding.puzzleGrid.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // TODO: 8/1/2025 Use position to determine which row and column were clicked; use to update viewModel 
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
      }
    });
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

//    viewModel.getSelectedDirection().observe(getViewLifecycleOwner(), dir -> {
//      binding.clueDirection.setText(dir.name());
//    });

    viewModel.getWords().observe(getViewLifecycleOwner(), words -> {
      // TODO: 8/1/2025 Pass PuzzleWords to adapter of clues 
    });
    // TODO: 8/1/2025 Observe LiveData containing board. Use the board to set number of columns in 
    //  GridView, pass board to adapter.setBoard()
  }
  
}
