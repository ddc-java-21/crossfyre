package edu.cnm.deepdive.crossfyre.view.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.FragmentScoped;
import edu.cnm.deepdive.crossfyre.R;
import edu.cnm.deepdive.crossfyre.databinding.ItemSquareBinding;
import java.util.Arrays;
import edu.cnm.deepdive.crossfyre.model.dto.PuzzleWord;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

@FragmentScoped
public class SquareAdapter extends ArrayAdapter<Character> {

  private final LayoutInflater inflater;

  @ColorInt
  private final int wallColor;

  // TODO: 8/1/25 create field for puzzleWords or at least wordStarts for the numbering of the cells
//  private final List<PuzzleWord> puzzleWord;

  @Inject
  SquareAdapter(@ActivityContext Context context) {
    super(context, R.layout.item_square);
    inflater = LayoutInflater.from(context);
    wallColor = getAttributeColor(R.attr.wallColor);
//    puzzleWord = new ArrayList<>();
    wallColor = getAttributeColor(R.attr.wallColor);
  }

  public SquareAdapter setBoard(Character[][] board) {
    clear();
    Arrays.stream(board)
        .flatMap(Arrays::stream)
        .forEach(this::add);
  public SquareAdapter setBoard(Character[][] board) {
    clear();
    // Using a stream to help us do a complex iteration
    // This would be like a for loop iterating then the column
    Arrays.stream(board)
        .flatMap(Arrays::stream)
        .forEach(this::add);
    notifyDataSetChanged();
    return this;
  }

  //What the gridview will invoke to know how to display the item at the position
  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    char c = getItem(position);
    ItemSquareBinding binding = (convertView != null)
    // if not null we want to bind whats already been inflated to our binding
    ? ItemSquareBinding.bind(convertView)
        // if were not inflating for an entire activity layout then we always use the three parameter form of inflate
        : ItemSquareBinding.inflate(inflater, parent, false);
    switch (c) {
      // represent what can be filled in
      case '_' -> {
        binding.square.setText("");
      }
      // if unicode then '\u0000'
      case '0' -> { // represent what can't be filled in
        binding.square.setText("");
        binding.getRoot().setBackgroundColor(wallColor);
      }
      default -> { // represent what we are going to put in the edit text for the guess
        binding.square.setText(String.valueOf(c));
      case '\u0000' -> {
        binding.square.setText("");
        binding.getRoot().setBackgroundColor(wallColor);
      }
      default -> {
        binding.square.setText(String.valueOf(c));
      }
    }
    // TODO: 8/1/25 If this position represents a wordStart then update the corresponding textView

    //once we've inflated the binding or bound it to an existing view item we return it and it will be displayed
    return binding.getRoot();
  }

  @ColorInt
  private int getAttributeColor(int colorAttrID) {
    TypedValue typedValue = new TypedValue();
    getContext().getTheme().resolveAttribute(colorAttrID, typedValue, true);
    return typedValue.data;
  }

}
