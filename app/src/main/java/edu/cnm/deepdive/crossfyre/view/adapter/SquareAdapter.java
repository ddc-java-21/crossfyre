package edu.cnm.deepdive.crossfyre.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.FragmentScoped;
import edu.cnm.deepdive.crossfyre.databinding.ItemSquareBinding;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

@FragmentScoped
public class SquareAdapter extends RecyclerView.Adapter<SquareAdapter.Holder> {


  private final LayoutInflater inflater;
  private final List<String> squares = new ArrayList<>();

  @Inject
  SquareAdapter(@ActivityContext Context context) {
    inflater = LayoutInflater.from(context);
  }

  public void setMask(String mask, int gridSize) {
    squares.clear();
    for (int i = 0; i < mask.length(); i++) {
      char c = mask.charAt(i);
      squares.add((c == '0') ? null : ""); // null for black cell, "" for empty
    }
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemSquareBinding binding = ItemSquareBinding.inflate(inflater, parent, false);
    return new Holder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(squares.get(position));
  }

  @Override
  public int getItemCount() {
    return squares.size();
  }

    static class Holder extends RecyclerView.ViewHolder {

      private final ItemSquareBinding binding;

      public Holder(@NonNull ItemSquareBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
      }

      public void bind(String content) {
        if (content == null) {
          // Black (dead) square
          binding.getRoot().setBackgroundColor(Color.BLACK);
          binding.square.setText("");
        } else {
          binding.getRoot().setBackgroundColor(Color.WHITE);
          binding.square.setText(content);
        }
      }
    }
}
