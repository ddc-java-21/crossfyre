package edu.cnm.deepdive.crossfyre.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.nimbusds.jose.shaded.gson.Gson;
import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.Puzzle.Board;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleRepository;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleWordRepository;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Profile({"service", "generate"})
public class PuzzleService implements AbstractPuzzleService {

  private final PuzzleRepository puzzleRepository;
  private final PuzzleWordRepository puzzleWordRepository;

  private final AbstractGeneratorService generatorService;

  private static final String API_KEY = "2fc887ac-578a-442a-b371-970eae934dfe";
  private static final String BASE_URL = "https://www.dictionaryapi.com/api/v3/references/collegiate/json/";

  private final OkHttpClient client = new OkHttpClient();
  private final Gson gson = new Gson();


  @Autowired
  PuzzleService(PuzzleRepository puzzleRepository, PuzzleWordRepository puzzleWordRepository,
      AbstractGeneratorService generatorService) {
    this.puzzleRepository = puzzleRepository;
    this.puzzleWordRepository = puzzleWordRepository;
    this.generatorService = generatorService;
  }


  // At 0:00 of every day Mon-Sun this method will invoke.
  // We need to get a new instance of the Puzzle object.
  // We need to get the size of the puzzle, assign the correct board layout for the puzzle day,
  //assign the correct date to the puzzle, and get the List<PuzzleWords> for that puzzle.
  @Profile("cronjob")
  @Scheduled(cron = "0 28 12 * * *") // Runs every day at midnight
  public void createPuzzle() {

    System.out.println("Entering createPuzzle:");

    // Create date for today and get value of the currentDay
    LocalDate today = LocalDate.now();
    int currentDayValue = today.getDayOfWeek().getValue(); // 1 = Monday ... 7 = Sunday

    // Map Monday-Sunday (1-7) to index in Board enum where index 0 = SUNDAY
    int boardIndex = currentDayValue % 7; // Sunday (7) → 0

    Board[] boards = Board.values();
//    Board todaysBoard = boards[boardIndex];
    Board todaysBoard = Board.TUESDAY;
    int boardSize = (int) Math.round(Math.sqrt(todaysBoard.day.length()));

    Instant date = today.atStartOfDay(ZoneOffset.UTC).toInstant();

    // Create the Puzzle
    Puzzle puzzle = new Puzzle();
    puzzle.setBoard(todaysBoard);
    puzzle.setDate(date);
    puzzle.setSize(boardSize);
    puzzle.setCreated(Instant.now());

    // Save puzzle first so it gets an ID

    // Fetch puzzle words
    // Untested try catch but crossword generator does work by itself in its class
//    try {
//      puzzleWords = CrosswordGenerator.generate(todaysBoard.toString(), puzzle.getSize());
//    } catch (IOException e) {
//      throw new RuntimeException(e);
//    }
    boolean valid = true;
    do {
      valid = true;
      Iterable<PuzzleWord> puzzleWords = generatorService.generatePuzzleWords(todaysBoard);
      Map<String, String> definitions = new HashMap<>();
      fetchDefinitions(puzzleWords, definitions);
      for (PuzzleWord puzzleWord : puzzleWords) {
        if (definitions.get(puzzleWord.getWordName()).equals("(No short definition or cross reference found)")
            || definitions.get(puzzleWord.getWordName()).equals("(No valid entry found)")
            || definitions.get(puzzleWord.getWordName()).equals("(Failed to fetch)")) {
          valid = false;
          break;
        }
      }
      if (valid) {
        for (PuzzleWord word : puzzleWords) {
          word.setPuzzle(puzzle);
          String clue = definitions.get(word.getWordName());   // TODO: 8/1/25 Do we want to truncate this or throw out puzzle/ swap in new shortdefs?
          if (clue.length() > 254) {
            clue = clue.substring(0, 254);
          }
          word.setClue(clue);
          puzzle.getPuzzleWords().add(word);
        }
        //puzzleWordRepository.saveAll(puzzleWords);
//      puzzle.getPuzzleWords().addAll(puzzleWords); // need to pass in a Collection, not an Iterable
        puzzleRepository.save(puzzle);
      }
    } while (!valid);

  }


  @Override
  public Puzzle get(Instant date) {
    return puzzleRepository
        .findByDate(date)
        .orElseThrow();
  }

  public void fetchDefinitions(Iterable<PuzzleWord> puzzleWords, Map<String, String> definitions) {

    for (PuzzleWord pw : puzzleWords) {
      try {
        String url = BASE_URL + pw.getWordName() + "?key=" + API_KEY;
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {

          if (response.isSuccessful()) {
            String json = response.body().string();
            JsonArray array = JsonParser.parseString(json).getAsJsonArray();

            if (!array.isEmpty() && array.get(0).isJsonObject()) {
              JsonObject entry = array.get(0).getAsJsonObject();

              JsonArray shortDefs = entry.getAsJsonArray("shortdef"); //
              JsonArray crossRefs = entry.getAsJsonArray("cxl");

              String definition;
              if (shortDefs != null && !shortDefs.isEmpty()) {
                definition = shortDefs.get(0).getAsString();
                definitions.put(pw.getWordName(), definition);
              } else {
                if (crossRefs != null && !crossRefs.isEmpty()) {
                  definition = crossRefs.get(0).getAsString();
                  definitions.put(pw.getWordName(), definition);
                }
                else {
                  definitions.put(pw.getWordName(), "(No short definition or cross reference found)");
                }
              }
            } else {
              definitions.put(pw.getWordName(), "(No valid entry found)");
            }
          } else {
            definitions.put(pw.getWordName(), "(Failed to fetch)");
          }
        }
      } catch (IOException | IllegalStateException | JsonParseException e) {
        definitions.put(pw.getWordName(), "(Error: " + e.getMessage() + ")");
      }
    }

  }
//  @Override
//  public Puzzle getOrAddPuzzle(Puzzle puzzle) {
//    return puzzleRepository
//        .findByDate(puzzle.getDate())
//        .or(() -> {
//
//          userPuzzle.setPuzzle(puzzle);
//          userPuzzle.setGuesses(new ArrayList<>());
//          userPuzzle.setSolved(false);
//          userPuzzleRepository.save(userPuzzle);
//          return Optional.of(userPuzzle);
//        })
//        .orElseThrow();
//  }

//  @Override
//  public

}
