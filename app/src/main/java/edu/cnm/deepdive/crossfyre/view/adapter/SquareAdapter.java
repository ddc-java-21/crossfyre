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
import javax.inject.Inject;

@FragmentScoped
public class SquareAdapter extends ArrayAdapter<Character> {

  private final LayoutInflater inflater;
  @ColorInt
  private final int wallColor;
  // TODO: 8/1/2025 Define a field for puzzleWord|wordStart 

  @Inject
  SquareAdapter(@ActivityContext Context context) {
    super(context, R.layout.item_square);
    inflater = LayoutInflater.from(context);
    wallColor = getAttributeColor(R.attr.wallColor);
  }

  public SquareAdapter setBoard(Character[][] board) {
    clear();
    Arrays.stream(board)
        .flatMap(Arrays::stream)
        .forEach(this::add);
    notifyDataSetChanged();
    return this;
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    char c = getItem(position);
    ItemSquareBinding binding = (convertView != null)
        ? ItemSquareBinding.bind(convertView)
        : ItemSquareBinding.inflate(inflater, parent, false);
    switch (c) {
      case '_' -> {
        binding.square.setText("");
      }
      case '\u0000' -> {
        binding.square.setText("");
        binding.getRoot().setBackgroundColor(wallColor);
      }
      default -> {
        binding.square.setText(String.valueOf(c));
      }
    }
    // TODO: 8/1/2025 If pos represents word start, update corresponding textView 
    return binding.getRoot();
  }

  @ColorInt
  private int getAttributeColor (int colorAttrId) {
    TypedValue typedValue = new TypedValue();
    getContext().getTheme().resolveAttribute(colorAttrId, typedValue, true);
    return typedValue.data;
  }

}
