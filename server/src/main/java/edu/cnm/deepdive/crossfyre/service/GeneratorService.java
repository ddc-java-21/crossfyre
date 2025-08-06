package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle.Board;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord.WordPosition;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * Generates crossword puzzle words based on a given board pattern and word list.
 * Loads words from a resource file grouped by word length, and uses a recursive backtracking
 * algorithm to place words on the board according to the pattern.
 */
@Component
public class GeneratorService implements AbstractGeneratorService {

  private static final String WORDS_FILE = "crossword/englishWordsWithTwoLetterWords.txt";
  private final Map<Integer, List<String>> wordListsMap;

  /**
   * Initializes a new instance of {@code GeneratorService}, loading and grouping words
   * by their length from a resource file.
   *
   * @throws IOException if the resource file cannot be read.
   */
  public GeneratorService() throws IOException {
    Resource resource = new ClassPathResource(WORDS_FILE);
    try (Stream<String> lines = Files.lines(Paths.get(resource.getURI()))) {
      wordListsMap = lines
          .filter((line) -> !line.isBlank())
          .map(String::strip)
          .map(String::toLowerCase)
          .collect(Collectors.groupingBy(String::length, HashMap::new,
              Collector.of(ArrayList::new, List::add, (list1, list2) -> {
                list1.addAll(list2);
                return list1;
              })
          ));
    }
  }

  /**
   * Generates a list of {@link PuzzleWord} instances that can be placed on the specified puzzle board.
   *
   * @param frame Board containing the pattern (0s and _s) representing blocked and open cells.
   * @return List of words placed on the board with their positions and directions.
   */
  @Override
  public List<PuzzleWord> generatePuzzleWords(Board frame) {
    String pattern = frame.day;
    int size = (int) Math.round(Math.sqrt(pattern.length()));

    Map<Integer, List<String>> candidatesMap = new HashMap<>();
    for (Map.Entry<Integer, List<String>> entry : wordListsMap.entrySet()) {
      candidatesMap.put(entry.getKey(), new ArrayList<>(entry.getValue()));
    }

    for (Map.Entry<Integer, List<String>> entry : candidatesMap.entrySet()) {
      Collections.shuffle(entry.getValue());
    }

    char[][] board = buildBoard(size, pattern);
    List<WordStart> starts = findStarts(size, board);

    Comparator<WordStart> wordLengthComparator = Comparator.comparingInt(WordStart::length)
        .reversed()
        .thenComparingInt(WordStart::row)
        .thenComparingInt(WordStart::column)
        .thenComparing(WordStart::direction);

    starts.sort(wordLengthComparator);
    Map<WordStart, String> placements = new TreeMap<>(wordLengthComparator);
    boolean success = generate(board, starts, candidatesMap, new HashSet<>(), placements);
    System.out.println(success ? "Success" : "Failed");
    System.out.println(placements);

    List<PuzzleWord> puzzleWords = new ArrayList<>();
    for (Map.Entry<WordStart, String> entry : placements.entrySet()) {
      WordStart wordStart = entry.getKey();
      String word = entry.getValue();
      PuzzleWord puzzleWord = new PuzzleWord();
      puzzleWord.setWordName(word);
      puzzleWord.setWordDirection(PuzzleWord.Direction.valueOf(String.valueOf(wordStart.direction)));
      puzzleWord.setWordPosition(new WordPosition(
          wordStart.row(),
          wordStart.column(),
          wordStart.length()
      ));
      puzzleWords.add(puzzleWord);
    }
    return puzzleWords;
  }

  /**
   * Finds valid starting points on the board for words in both ACROSS and DOWN directions.
   *
   * @param size  Size of the board (rows and columns).
   * @param board 2D character array representing the board pattern.
   * @return List of potential word starts with positions and directions.
   */
  private static List<WordStart> findStarts(int size, char[][] board) {
    List<WordStart> starts = new LinkedList<>();
    for (int row = 0; row < size; row++) {
      boolean wallAbove = (row == 0);
      for (int column = 0; column < size; column++) {
        boolean wallLeft = (column == 0 || board[row][column - 1] == '0');
        if (board[row][column] != '0' && wallLeft) {
          int length = 0;
          for (int wordColumn = column; wordColumn < size && board[row][wordColumn] != '0'; wordColumn++, length++) {
            // count length
          }
          if (length > 1) {
            starts.add(new WordStart(row, column, length, Direction.ACROSS));
          }
        }
        if (board[row][column] != '0' && (wallAbove || board[row - 1][column] == '0')) {
          int length = 0;
          for (int wordRow = row; wordRow < size && board[wordRow][column] != '0'; wordRow++, length++) {
            // count length
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

  /**
   * Converts a linear string pattern into a 2D character array representing the board.
   *
   * @param size    Size of the board.
   * @param pattern Linear pattern representing board layout.
   * @return 2D array of board characters.
   */
  private static char[][] buildBoard(int size, String pattern) {
    char[][] board = new char[size][];
    for (int i = 0; i < size; i++) {
      board[i] = pattern.substring(i * size, (i + 1) * size).toCharArray();
    }
    return board;
  }

  /**
   * Recursively attempts to place words from the candidate list into the board to fill all word slots.
   *
   * @param board        Current state of the board.
   * @param starts       List of available word starting points.
   * @param candidatesMap Map of candidate words by length.
   * @param usedWords    Set of already used words to avoid repeats.
   * @param placements   Map to store word placements.
   * @return true if successful in placing all words, false otherwise.
   */
  private static boolean generate(char[][] board, List<WordStart> starts, Map<Integer, List<String>> candidatesMap,
      Set<String> usedWords, Map<WordStart, String> placements) {

    return starts.isEmpty() || candidatesMap.get(starts.getFirst().length())
        .stream()
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
          boolean success = generate(trialBoard, starts.subList(1, starts.size()), candidatesMap,
              trialUsedWords, placements);
          if (!success) {
            placements.remove(start);
          }
          return success;
        });
  }

  /**
   * Enum representing word directions (ACROSS or DOWN), with corresponding row and column offsets.
   */
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

  /**
   * Record representing a starting point for a word, including its position, length, and direction.
   * Implements {@link Comparable} to enable natural ordering by direction, row, and column.
   */
  private record WordStart(int row, int column, int length, Direction direction) implements Comparable<WordStart> {

    private static final Comparator<WordStart> COMPARATOR = Comparator.comparing(WordStart::direction)
        .thenComparingInt(WordStart::row)
        .thenComparingInt(WordStart::column);

    @Override
    public int compareTo(@NotNull WordStart other) {
      return COMPARATOR.compare(this, other);
    }
  }
}
