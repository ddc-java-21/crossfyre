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

/**
 * Adapter for displaying a crossword puzzle grid of squares in a {@link GridView}.
 * Handles rendering of cell appearance, text entry, highlights, and word numbering.
 */
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

  /**
   * Constructs an instance of {@link SquareAdapter} for a crossword puzzle grid.
   *
   * @param context Activity context injected by Hilt.
   */
  @Inject
  SquareAdapter(@ActivityContext Context context) {
    super(context, R.layout.item_square);
    inflater = LayoutInflater.from(context);
    wallColor = getAttributeColor(R.attr.wallColor);
    spaceColor = getAttributeColor(R.attr.spaceColor);
    wordHighlightColor = getAttributeColor(R.attr.wordHighlightColor);
    selectionHighlightColor = getAttributeColor(R.attr.selectionHighlightColor);
    highlightedPositions = new ArrayList<>();
    wordStarts = new int[0];
    selectedPosition = -1;
    guesses = new LinkedList<>();
  }

  /**
   * Inflates or reuses the view for a single cell in the grid.
   *
   * @param position    Index of the item in the adapter.
   * @param convertView Reusable view (if any).
   * @param parent      Parent view group.
   * @return Inflated or reused view for the cell.
   */
  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    boolean open = getItem(position);
    ItemSquareBinding binding = (convertView != null)
        ? ItemSquareBinding.bind(convertView)
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

    int foundPosition = Arrays.binarySearch(wordStarts, position);
    if (foundPosition >= 0) {
      binding.cellWordStartNumber.setText(String.valueOf(foundPosition + 1));
      binding.cellWordStartNumber.setVisibility(View.VISIBLE);
    } else {
      binding.cellWordStartNumber.setVisibility(View.INVISIBLE);
    }

    return binding.getRoot();
  }

  /**
   * Gets a resolved color value from the theme using an attribute ID.
   *
   * @param colorAttrID Color attribute resource ID.
   * @return Resolved color integer.
   */
  @ColorInt
  private int getAttributeColor(int colorAttrID) {
    TypedValue typedValue = new TypedValue();
    getContext().getTheme().resolveAttribute(colorAttrID, typedValue, true);
    return typedValue.data;
  }

  /**
   * Returns the current list of highlighted cell positions.
   *
   * @return List of highlighted cell indices.
   */
  public List<Integer> getHighlightedPositions() {
    return highlightedPositions;
  }

  /**
   * Sets the highlighted positions to the specified list.
   *
   * @param highlightedPositions New list of positions to highlight.
   */
  public void setHighlightedPositions(List<Integer> highlightedPositions) {
    this.highlightedPositions.clear();
    this.highlightedPositions.addAll(highlightedPositions);
    notifyDataSetChanged();
  }

  /**
   * Sets the positions that are the starting cells of words.
   *
   * @param wordStarts Array of word start positions.
   */
  public void setWordStarts(int[] wordStarts) {
    this.wordStarts = wordStarts.clone();
    notifyDataSetChanged();
  }

  /**
   * Sets the selected cell position.
   *
   * @param selectedPosition Index of selected cell.
   */
  public void setSelectedPosition(int selectedPosition) {
    this.selectedPosition = selectedPosition;
    notifyDataSetChanged();
  }

  /**
   * Converts a 2D board of booleans into the flat data for this adapter.
   *
   * @param board 2D boolean array where {@code true} indicates an open square.
   * @return This adapter for chaining.
   */
  public SquareAdapter setGrid(boolean[][] board) {
    this.size = board.length;
    clear();
    Arrays.stream(board)
        .forEach((row) -> {
          for (boolean open : row) {
            add(open);
          }
        });
    notifyDataSetChanged();
    return this;
  }

  /**
   * Sets the current list of guesses to be rendered on the grid.
   *
   * @param guesses List of user guesses.
   */
  public void setGuesses(List<Guess> guesses) {
    this.guesses.clear();
    this.guesses.addAll(guesses);
    notifyDataSetChanged();
  }

  /**
   * Sets the listener to be notified when the user enters a guess.
   *
   * @param listener Listener for guess entry.
   */
  public void setListener(OnGuessListener listener) {
    this.listener = listener;
  }

  /**
   * Interface definition for a callback to be invoked when a guess is made.
   */
  public interface OnGuessListener {

    /**
     * Called when a character is guessed by the user.
     *
     * @param guess  Character guessed.
     * @param row    Row of the guessed cell.
     * @param column Column of the guessed cell.
     */
    void onGuess(char guess, int row, int column);
  }

}
