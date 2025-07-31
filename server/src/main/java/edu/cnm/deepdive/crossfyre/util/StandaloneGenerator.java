package edu.cnm.deepdive.crossfyre.util;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle.Board;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@Profile("generate")
public class StandaloneGenerator implements CommandLineRunner {

  private static final String WORDS_FILE = "crossword/common-english-words.txt";
  private final List<String> words;

  public StandaloneGenerator() throws IOException {
    Resource resource = new ClassPathResource(WORDS_FILE);
    try (Stream<String> lines = Files.lines(Paths.get(resource.getURI()))) {
      words = lines
          .filter((line) -> !line.isBlank())
          .map(String::strip)
          .map(String::toLowerCase)
          .toList();
    }
  }

  @Override
  public void run(String... args) throws Exception {
    String pattern = Board.MONDAY.day;
    int size = (int) Math.round(Math.sqrt(pattern.length()));
    List<String> candidates = getCandidates(size);

    char[][] board = buildBoard(size, pattern);

    List<WordStart> starts = findStarts(size, board);

    Map<WordStart, String> placements = new TreeMap<>();
    boolean success = generate(board, starts, candidates, new HashSet<>(), placements);
    System.out.println(placements);
  }

  private static List<WordStart> findStarts(int size, char[][] board) {
    List<WordStart> starts = new LinkedList<>();
    for (int row = 0; row < size; row++) {
      boolean wallAbove = (row == 0);
      for (int column = 0; column < size; column++) {
        boolean wallLeft = (column == 0 || board[row][column - 1] == '0');
        if (board[row][column] != '0' && wallLeft) {
          int length = 0;
          for (int wordColumn = column; wordColumn < size && board[row][wordColumn] != '0'; wordColumn++, length++) {
            // do nothing
          }
          if (length > 1) {
            starts.add(new WordStart(row, column, length, Direction.ACROSS));
          }
        }
        if (board[row][column] != '0' && (wallAbove || board[row - 1][column] == '0')) {
          int length = 0;
          for (int wordRow = row; wordRow < size && board[wordRow][column] != '0'; wordRow++, length++) {
            // do nothing
          }
          if (length > 1) {
            starts.add(new WordStart(row, column, length, Direction.DOWN));
          }
        }
      }
    }
    starts.sort(null);
    return starts;
  }

  @NotNull
  private List<String> getCandidates(int size) {
    List<String> candidates = words
        .stream()
        .filter((word) -> word.length() <= size)
        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    Collections.shuffle(candidates);
    return candidates;
  }

  private static char[][] buildBoard(int size, String pattern) {
    char[][] board = new char[size][];
    for (int i = 0; i < size; i++) {
      board[i] = pattern.substring(i * size, (i + 1) * size).toCharArray();
    }
    return board;
  }

  private static boolean generate(char[][] board, List<WordStart> starts, List<String> candidates,
      Set<String> usedWords, Map<WordStart, String> placements) {

    return starts.isEmpty() || candidates
        .stream()
        .filter((word) -> word.length() == starts.getFirst().length())
        .filter((word) -> !usedWords.contains(word))
        .anyMatch((word) -> {
          WordStart start = starts.getFirst();
          for (int i = 0; i < start.length(); i++) {
            int row = start.row() + start.direction().rowOffset() * i;
            int column = start.column() + start.direction().columnOffset() * i;
            if (board[row][column] == '0'
                || board[row][column] != '_'
                && board[row][column] != word.charAt(i)) {
              return false;
            }
          }
          Set<String> trialUsedWords = new HashSet<>(usedWords);
          trialUsedWords.add(word);
          placements.put(start, word);
          char[][] trialBoard = Stream.of(board)
              .map(char[]::clone)
              .toArray(char[][]::new);
          for (int i = 0; i < start.length(); i++) {
            int row = start.row() + start.direction().rowOffset() * i;
            int column = start.column() + start.direction().columnOffset() * i;
            trialBoard[row][column] = word.charAt(i);
          }
          return generate(trialBoard, starts.subList(1, starts.size()), candidates,
              trialUsedWords, placements);
        });
  }


  private enum Direction {
    ACROSS(0, 1),
    DOWN(1, 0);

    private final int rowOffset;
    private final int columnOffset;

    Direction(int rowOffset, int columnOffset) {
      this.rowOffset = rowOffset;
      this.columnOffset = columnOffset;
    }

    public int rowOffset() {
      return rowOffset;
    }

    public int columnOffset() {
      return columnOffset;
    }

  }
  private record WordStart(int row, int column, int length, Direction direction) implements Comparable<WordStart> {

    private static final Comparator<WordStart> COMPARATOR = Comparator.comparingInt(WordStart::row)
        .thenComparingInt(WordStart::column)
        .thenComparing(WordStart::direction);

    @Override
    public int compareTo(@NotNull WordStart other) {
      return COMPARATOR.compare(this, other);
    }

  }

}
