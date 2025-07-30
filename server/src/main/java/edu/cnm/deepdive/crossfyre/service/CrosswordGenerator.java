package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord.Direction;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord.WordPosition;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

public class CrosswordGenerator {
	
	static int[][] word_starts;
	static final int WALL = -1;
	static final int BLANK = 0;
	static final int ACROSS = 1;
	static final int DOWN = 2;
	static final int BOTH = 3;
	
	static ArrayList<String> wordsUsed = new ArrayList<>();
	static List<PuzzleWord> puzzleWords = new ArrayList<>();
	static ArrayList<String> words = new ArrayList<>();
	static ArrayList<Integer> counts = new ArrayList<>();
	
	static boolean[][][] boards;
	
	public static void loadWords() throws IOException {
    try (InputStream inputStream = CrosswordGenerator.class.getClassLoader()
        .getResourceAsStream("crossword/common-english-words.txt")) {
			InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
			BufferedReader f = new BufferedReader(reader);
			while (true) {
				String word = f.readLine();
				if (word == null) {
					break;
				}
				words.add(word);
			}
		}
	}

	static int findWordLength(int[][] word_starts, int i, int j,
			boolean is_across) {
		int len;
		for (len = 1; len < word_starts.length; len++) {
			if (is_across) {
				j++;
			} else {
				i++;
			}
			if (i >= word_starts.length || j >= word_starts.length || word_starts[i][j] == WALL) {
				break;
			}
		}
		return len;
	}

	private static boolean tryToSolve(ArrayList<WordStart> word_starts, char[][] board, int cur_word_start) {
		if (cur_word_start >= word_starts.size()) {
			return true;
		}
		
		counts.set(cur_word_start, counts.get(cur_word_start) + 1);
		WordStart ws = word_starts.get(cur_word_start);

		// Remember which characters in this word were already set
		boolean[] had_letter = new boolean[ws.length];
		for (int i = 0; i < ws.length; i++) {
			had_letter[i] = true;
		}
		int r=ws.i;
		int c=ws.j;
		for (int i = 0; i < ws.length; i++) {
			if (board[r][c] == 0) {
				had_letter[i] = false;
			}
			if (ws.is_across)
				c++;
			else
				r++;
		}
		
		// Find a word that fits here, given the letters already on the board
    for (String word : words) {
      if (!wordsUsed.contains(word) && word.length() == ws.length) {
        boolean word_fits = true;
        r = ws.i;
        c = ws.j;
        for (int j = 0; j < ws.length; j++) {
          if (board[r][c] != 0 && board[r][c] != word.charAt(j)) {
            word_fits = false;
            break;
          }
          if (ws.is_across)
            c++;
          else
            r++;
        }

        if (word_fits) {
          // Place this word on the board
          wordsUsed.add(word);
          PuzzleWord puzzleWord = new PuzzleWord();
          puzzleWord.setWordName(word);
          puzzleWord.setWordDirection(ws.is_across ? Direction.ACROSS : Direction.DOWN);
          puzzleWord.setWordPosition(new WordPosition(
              ws.i,
              ws.j,
              ws.length
          ));
          puzzleWords.add(puzzleWord);
          r = ws.i;
          c = ws.j;
          for (int j = 0; j < ws.length; j++) {
            board[r][c] = word.charAt(j);
            if (ws.is_across)
              c++;
            else
              r++;
          }

          // If puzzle can be solved this way, we're done
          if (tryToSolve(word_starts, board, cur_word_start + 1)) {
            return true;
          }

          // If not, take up letters that we placed and try a different word
          r = ws.i;
          c = ws.j;
          for (int j = 0; j < ws.length; j++) {
            if (!had_letter[j])
              board[r][c] = 0;
            if (ws.is_across)
              c++;
            else
              r++;
          }

          wordsUsed.remove(word);
          puzzleWords.removeLast();
        }
      }
    }
			
		// If no word can work, return false.
		return false;
		
	}

	private static String printSolution(int[][] word_starts, char[][] board) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < word_starts.length; i++) {
			for (int j = 0; j < word_starts[0].length; j++) {
				int ws=word_starts[i][j];
				if(ws==WALL){
					builder.append("_ ");
				} else {
					builder.append(board[i][j]).append(" ");
				}
			}
			builder.append("\n");
		}
		return builder.toString();
	}

	static void printStarts(int[][] word_starts) {
    for (int[] wordStart : word_starts) {
      for (int j = 0; j < word_starts[0].length; j++) {
        int ws = wordStart[j];
        if (ws == WALL) {
          System.out.print("W ");
        }
        if (ws == BLANK) {
          System.out.print("  ");
        }
        if (ws == ACROSS) {
          System.out.print("- ");
        }
        if (ws == DOWN) {
          System.out.print("| ");
        }
        if (ws == BOTH) {
          System.out.print("+ ");
        }
      }
      System.out.println();
    }
	}
	
	private static void loadBoards() {
	
		boolean[][] board1 = {
			{false, true, true, true, true},
			{true, true, true, true, true},
			{true, true, true, true, true},
			{true, true, true, true, false},
			{true, true, true, true, false},
		};
		boolean[][] board2 = {
			{true, true, true, true, true},
			{true, true, true, true, true},
			{true, true, true, true, true},
			{true, true, true, true, false},
			{true, true, true, true, false},
		};
		boolean[][] board3 = {
			{false, true, true, true, false},
			{false, true, true, true, false},
			{true, true, true, true, true},
			{true, true, true, true, true},
			{true, true, true, true, true},
		};
		boolean[][] board4 = {
			{false, false, true, true, true},
			{true, true, true, true, true},
			{true, true, true, true, true},
			{true, true, true, true, true},
			{true, true, true, false, false},
		};
		boolean[][] board5 = {
			{true, true, true, false, false},
			{true, true, true, true, true},
			{true, true, true, true, true},
			{true, true, true, true, true},
			{false, false, true, true, true},
		};
		boolean[][] board6 = {
			{false, true, true, true, false},
			{true, true, true, true, true},
			{true, true, true, true, true},
			{true, true, true, true, true},
			{false, true, true, true, false},
		};
		boolean[][] board7 = {
			{true, true, true, true, false},
			{true, true, true, true, false},
			{true, true, true, true, true},
			{false, true, true, true, true},
			{false, true, true, true, true},
		};
		boolean[][] board8 = {
			{true, true, true, false, false},
			{true, true, true, true, false},
			{true, true, true, true, true},
			{false, true, true, true, true},
			{false, false, true, true, true},
		};

		
		boolean[][] board9 = {
				{false, false, true, true, true, true},
				{true, true, true, false, true, false},
				{true, true, true, true, true, false},
				{true, false, true, true, true, true},
				{false, true, true, true, false, true},
				{true, true, true, false, false, true},
		};
		
		boards = new boolean[][][]{
			board1,
			board2,
			board3,
			board4,
			board5,
			board6,
			board7,
			board8,
			board9
		};
	
	}
	
	public static void main(String[] args) throws IOException {

		generate();

	}

	public static List<PuzzleWord> generate() throws IOException {

		loadWords();
		loadBoards();

		puzzleWords.clear();
		wordsUsed.clear();

		boolean[][] hasLetter = boards[8];

		word_starts = new int[hasLetter.length][hasLetter.length];
		char[][] board = new char[hasLetter.length][hasLetter.length];

		for (int i = 0; i < word_starts.length; i++) {
			for (int j = 0; j < word_starts[0].length; j++) {
				if (!hasLetter[i][j]) {
					word_starts[i][j] = WALL;
				} else {
					word_starts[i][j] = BLANK;
				}
			}
		}
		for (int i = 0; i < word_starts.length; i++) {
			for (int j = 0; j < word_starts[0].length; j++) {
				int thing=0;
				if(!hasLetter[i][j]){
					continue;
				}
				if((i==0 || !hasLetter[i - 1][j]) && // there is a wall above us AND
						(i < 5 && hasLetter[i + 1][j])) { // no wall below us
					word_starts[i][j]=DOWN;
					thing++;
				}
				if((j==0 || !hasLetter[i][j - 1]) &&
						(j < 5 && hasLetter[i][j + 1])){
					if(thing==1){
						word_starts[i][j]=BOTH;
					}
					else{
						word_starts[i][j]=ACROSS;
					}
				}
			}
		}

		System.out.println("Word starts:");
		printStarts(word_starts);
		System.out.println();

		boolean puzzleGenerated = false;
		int attemptCount = 0;
		while (!puzzleGenerated) {
			RandomGenerator rng = new SecureRandom();
			List<String> wordsBackup = new ArrayList<>(words);
			List<String> newList = rng.ints(words.size(), 0, words.size())
				.mapToObj(words::get)
				.sorted(Comparator.comparingInt(String::length))
				.filter ((word) -> word.length() <= hasLetter.length)
				.collect(Collectors.toList());
			Collections.reverse(newList);
			words = new ArrayList<>(newList);
			System.out.println("Iteration: " + attemptCount);
			attemptCount++;
			counts = null;
			wordsUsed = null;
			counts = new ArrayList<>();
			wordsUsed = new ArrayList<>();

			ArrayList<WordStart> word_start_list = new ArrayList<>();
			for (int i = 0; i < word_starts.length; i++) {
				for (int j = 0; j < word_starts[0].length; j++) {
					if (word_starts[i][j] == ACROSS || word_starts[i][j] == BOTH) {
						word_start_list.add(new WordStart(i, j, true, findWordLength(word_starts, i, j, true)));
					}
					if (word_starts[i][j] == DOWN || word_starts[i][j] == BOTH) {
						word_start_list.add(new WordStart(i, j, false, findWordLength(word_starts, i, j, false)));
					}
				}
			}

			counts = new ArrayList<>();
			for (int i = 0; i < word_start_list.size(); i++) {
				counts.add(0);
			}

			if (tryToSolve(word_start_list, board, 0)) {
				System.out.println("Solution:");
				System.out.println(printSolution(word_starts, board));
				System.out.println();
				puzzleGenerated = true;
			} else {
				System.out.println("NO SOLUTION FOUND");
			}

			words = new ArrayList<>(wordsBackup);
		}
		System.out.println("words:");
		for (String s : wordsUsed) {
			System.out.println(s);
		}
		for (PuzzleWord pz : puzzleWords) {
			System.out.println(pz.getWordName() + " " + pz.getWordDirection() + " " + pz.getWordPosition());
		}
		System.out.println(printSolution(word_starts, board));
		return puzzleWords;
	}

}

class WordStart {
	public WordStart(int i, int j, boolean is_across, int length) {
		this.i = i;
		this.j = j;
		this.is_across = is_across;
		this.length = length;
	}
	int i;
	int j;
	boolean is_across; // if false, this is a down start
	int length;
}
