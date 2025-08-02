package edu.cnm.deepdive.crossfyre.view.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import dagger.hilt.android.qualifiers.ActivityContext;
import edu.cnm.deepdive.crossfyre.model.dto.PuzzleWord;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class PuzzleClueAdapter extends ArrayAdapter<PuzzleWord> {

  private final List<PuzzleWord> puzzleWords;

  @Inject
  public PuzzleClueAdapter(@ActivityContext Context context) {
    super(context, 0);
    puzzleWords = new ArrayList<>();
  }

  public PuzzleWord splitClues() {

  }

}