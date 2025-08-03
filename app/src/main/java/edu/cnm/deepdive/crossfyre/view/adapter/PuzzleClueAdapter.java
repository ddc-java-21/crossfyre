package edu.cnm.deepdive.crossfyre.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import dagger.hilt.android.qualifiers.ActivityContext;
import edu.cnm.deepdive.crossfyre.R;
import edu.cnm.deepdive.crossfyre.model.dto.PuzzleWord;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class PuzzleClueAdapter extends ArrayAdapter<PuzzleWord> {

  private final List<PuzzleWord> puzzleWords;
  private final MutableLiveData<List<PuzzleWord>> acrossClues = new MutableLiveData<>(new ArrayList<>());
  private final MutableLiveData<List<PuzzleWord>> downClues = new MutableLiveData<>(new ArrayList<>());

  @Inject
  public PuzzleClueAdapter(@ActivityContext Context context) {
    super(context, 0);
    puzzleWords = new ArrayList<>();
  }

  public LiveData<List<PuzzleWord>> getAcrossClues() {
    return acrossClues;
  }

  public LiveData<List<PuzzleWord>> getDownClues() {
    return downClues;
  }

  public void setPuzzleWords(List<PuzzleWord> words) {
    List<PuzzleWord> across = new ArrayList<>();
    List<PuzzleWord> down = new ArrayList<>();

    for (PuzzleWord word : words) {
      if (word.getWordDirection() == PuzzleWord.Direction.ACROSS) {
        across.add(word);
      } else if (word.getWordDirection() == PuzzleWord.Direction.DOWN) {
        down.add(word);
      }
    }
    acrossClues.setValue(across);
    downClues.setValue(down);
    clear();
    addAll(across);
    addAll(down);
    notifyDataSetChanged();
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View itemView = convertView;
    if (itemView == null) {
      itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_clue, parent, false);
    }

    PuzzleWord word = getItem(position);
    if (word != null) {
      TextView clueText = itemView.findViewById(R.id.clue_text);
    }

    return itemView;
  }
}