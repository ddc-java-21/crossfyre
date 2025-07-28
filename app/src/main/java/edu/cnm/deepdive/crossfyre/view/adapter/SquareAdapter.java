package edu.cnm.deepdive.crossfyre.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.FragmentScoped;
import edu.cnm.deepdive.crossfyre.databinding.ItemSquareBinding;
import edu.cnm.deepdive.crossfyre.model.dto.PuzzleWord;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

@FragmentScoped
public class SquareAdapter extends Adapter<ViewHolder> {

  private final List<PuzzleWord> words;
  private final LayoutInflater inflater;

  @NonNull
  private OnClickListener clickListener;
  @NonNull
  private OnLongClickListener longClickListener;

  @Inject
  SquareAdapter(@ActivityContext Context context) {
    words = new ArrayList<>();
    inflater = LayoutInflater.from(context);
    clickListener = (word, position) -> {};
  }


  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup container, int i) {
    ItemSquareBinding binding =ItemSquareBinding.inflate(inflater, container, false);
    return new Holder(binding, clickListener, longClickListener);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

  }

  @Override
  public int getItemCount() {
    return words.size();
  }

  private static class Holder extends ViewHolder {

    private final ItemSquareBinding binding;

    private OnClickListener clickListener;
    private OnLongClickListener longClickListener;

    Holder(@NonNull ItemSquareBinding binding,
        @NonNull OnClickListener clickListener, @NonNull OnLongClickListener longClickListener) {
      super(binding.getRoot());
      this.binding =binding;
      this.clickListener = clickListener;
      this.longClickListener = longClickListener;
    }

    void bind(int positionRow, int positionCol, PuzzleWord word) {
      binding.title.setText(note.getTitle());
      String noteDescription = note.getDescription();
      binding.description.setText(noteDescription != null ? noteDescription : "");
      binding.created.setText(
          formatter.format(
              ZonedDateTime.ofInstant(note.getCreated(), ZoneId.systemDefault())));
      binding.thumbnail.setVisibility((View.GONE));
      // TODO: 6/17/2025 Display Thumbnail
      binding
          .getRoot()
          .setOnClickListener((v) -> listener.onNoteClick(note, position));
    }


  }

  public interface OnNoteClickListener {
    void onNoteClick(NoteWithImages note, int position);

  }
}
