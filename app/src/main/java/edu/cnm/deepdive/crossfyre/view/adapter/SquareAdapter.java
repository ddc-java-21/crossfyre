package edu.cnm.deepdive.crossfyre.view.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.FragmentScoped;
import edu.cnm.deepdive.crossfyre.R;
import edu.cnm.deepdive.crossfyre.databinding.ItemSquareBinding;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzleDto.Guess;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzleDto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;

@FragmentScoped
public class SquareAdapter extends ArrayAdapter<Boolean> {

  private final LayoutInflater inflater;
  private final List<Integer> highlightedPositions;
  private int[] wordStarts;
  private int selectedPosition;
  private final List<UserPuzzleDto.Guess> guesses;

  private OnGuessListener listener;

  @ColorInt
  private final int wallColor;

  @ColorInt
  private final int spaceColor;

  @ColorInt
  private final int wordHighlightColor;

  @ColorInt
  private final int selectionHighlightColor;
  private int size;

  // TODO: 8/1/25 create field for puzzleWords or at least wordStarts for the numbering of the cells
//  private final List<PuzzleWord> puzzleWord;

  @Inject
  SquareAdapter(@ActivityContext Context context) {
    super(context, R.layout.item_square);
    inflater = LayoutInflater.from(context);
//    puzzleWord = new ArrayList<>();
    wallColor = getAttributeColor(R.attr.wallColor);
    spaceColor = getAttributeColor(R.attr.spaceColor);
    wordHighlightColor = getAttributeColor(R.attr.wordHighlightColor);
    selectionHighlightColor = getAttributeColor(R.attr.selectionHighlightColor);
    highlightedPositions = new ArrayList<>();
    wordStarts = new int[0];
    selectedPosition = -1;
    guesses = new LinkedList<>();
  }

  // write method in viewmodel that would take big object adn get teh piece of the object we want to use and use transformations.map
  //What the gridview will invoke to know how to display the item at the position

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    boolean open = getItem(position);
    ItemSquareBinding binding = (convertView != null)
        // if not null we want to bind whats already been inflated to our binding
        ? ItemSquareBinding.bind(convertView)
        // if were not inflating for an entire activity layout then we always use the three parameter form of inflate
        : ItemSquareBinding.inflate(inflater, parent, false);

    binding.getRoot().setLayoutParams(new GridView.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    ));

    binding.getRoot().setBackgroundResource(0);

    int row = position / size;
    int col = position % size;

    if (open) {
      String guess = guesses
          .stream()
          .filter((g) -> g.getGuessPosition().getRow() == row
              && g.getGuessPosition().getColumn() == col)
          .map(g -> String.valueOf(g.getGuess()))
          .findFirst()
          .orElse("");
      binding.square.setText(guess);
      binding.staticSquare.setText(guess);
      binding.square.setVisibility(View.INVISIBLE);
      binding.staticSquare.setVisibility(View.VISIBLE);
      binding.getRoot().setBackgroundColor(spaceColor);
    } else {
      binding.square.setText("");
      binding.staticSquare.setText("");
      binding.square.setVisibility(View.INVISIBLE);
      binding.staticSquare.setVisibility(View.INVISIBLE);
      binding.getRoot().setBackgroundColor(wallColor);
    }

    if (highlightedPositions.contains(position)) {
      binding.getRoot().setBackgroundColor(wordHighlightColor);
    }
    if (position == selectedPosition) {
      binding.square.requestFocus();
      binding.square.selectAll();
      binding.getRoot().setBackgroundColor(selectionHighlightColor);
      Editable editable = binding.square.getText();
      OnFocusChangeListener listener = (view, hasFocus) -> {
        if (!hasFocus) {
          String guess = editable.toString().strip();
          if (!guess.isEmpty()) {
            this.listener.onGuess(guess.charAt(0), row, col);
          }
        }
      };
      binding.square.setOnFocusChangeListener(listener);

      binding.square.setVisibility(View.VISIBLE);
      binding.staticSquare.setVisibility(View.INVISIBLE);
    }

    // TODO: 8/1/25 If this position represents a wordStart then update the corresponding textView
    // TODO: Assign clue number if this is a word start
    int foundPosition = Arrays.binarySearch(wordStarts, position);
    if (foundPosition >= 0) {
      binding.cellWordStartNumber.setText(String.valueOf(foundPosition + 1));
      binding.cellWordStartNumber.setVisibility(View.VISIBLE);
    } else {
      binding.cellWordStartNumber.setVisibility(View.INVISIBLE);
    }

    //once we've inflated the binding or bound it to an existing view item we return it and it will be displayed
    return binding.getRoot();
  }

  @ColorInt
  private int getAttributeColor(int colorAttrID) {
    TypedValue typedValue = new TypedValue();
    getContext().getTheme().resolveAttribute(colorAttrID, typedValue, true);
    return typedValue.data;
  }

  public List<Integer> getHighlightedPositions() {
    return highlightedPositions;
  }

  public void setHighlightedPositions(
      List<Integer> highlightedPositions) {
    this.highlightedPositions.clear();
    this.highlightedPositions.addAll(highlightedPositions);
    notifyDataSetChanged();
  }

  public void setWordStarts(int[] wordStarts) {
    this.wordStarts = wordStarts.clone();
    notifyDataSetChanged();
  }

  public void setSelectedPosition(int selectedPosition) {
    this.selectedPosition = selectedPosition;
    notifyDataSetChanged();
  }

  public SquareAdapter setGrid(boolean[][] board) {
    this.size = board.length;
    clear();
    // Using a stream to help us do a complex iteration
    // This would be like a for loop iterating then the column
    Arrays.stream(board)
        .forEach((row) -> {
          for (boolean open : row) {
            add(open);
          }
        });
    notifyDataSetChanged();
    return this;
  }

  public void setGuesses(List<Guess> guesses) {
    this.guesses.clear();
    this.guesses.addAll(guesses);
    notifyDataSetChanged();
  }

  public void setListener(OnGuessListener listener) {
    this.listener = listener;
  }

  public interface OnGuessListener {
    void onGuess(char guess, int row, int column);
  }

}
