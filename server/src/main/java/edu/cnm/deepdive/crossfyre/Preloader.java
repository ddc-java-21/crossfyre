package edu.cnm.deepdive.crossfyre;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.cnm.deepdive.crossfyre.model.dto.endpoint.Definition;
import edu.cnm.deepdive.crossfyre.model.dto.preload.PuzzlePreloadDto;
import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord.WordPosition;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleRepository;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleWordRepository;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * Loads initial puzzle and word data into the database from preconfigured JSON files when the
 * application is run with the {@code preload} Spring profile.
 *
 * <p>This component is used to preload data for development, testing, or demonstration purposes.
 * It reads a puzzle and its associated words from classpath resources (typically JSON files),
 * parses them into entity objects, and persists them using the provided repositories.</p>
 *
 * <p>Expected configuration properties:</p>
 * <ul>
 *   <li>{@code crossfyre.preload.file.puzzle} - path to the JSON file containing puzzle metadata.</li>
 *   <li>{@code crossfyre.preload.file.words} - path to the JSON file containing puzzle words (currently unused).</li>
 * </ul>
 *
 * <p>Only runs when the "preload" profile is active.</p>
 */
@Component
@Profile("preload")
public class Preloader implements CommandLineRunner {

  private static final String DEFINITION_URL = "https://www.dictionaryapi.com/api/v3/references/collegiate/json/voluminous?key=";

  private final PuzzleRepository repository;
  private final PuzzleWordRepository puzzleWordRepository;
  private final String puzzlePreloadFile;
  private final String wordsPreloadFile;

  /**
   * Creates an instance of {@code Preloader} with injected dependencies and file path values.
   *
   * @param repository         Repository for persisting {@link Puzzle} entities.
   * @param puzzleWordRepository Repository for persisting {@link PuzzleWord} entities.
   * @param puzzlePreloadFile  Path to the puzzle preload JSON file.
   * @param wordsPreloadFile   Path to the words preload JSON file (currently unused).
   */
  @Autowired
  public Preloader(PuzzleRepository repository,
      PuzzleWordRepository puzzleWordRepository,
      @Value("${crossfyre.preload.file.puzzle}") String puzzlePreloadFile,
      @Value("${crossfyre.preload.file.words}") String wordsPreloadFile) {
    this.repository = repository;
    this.puzzleWordRepository = puzzleWordRepository;
    this.puzzlePreloadFile = puzzlePreloadFile;
    this.wordsPreloadFile = wordsPreloadFile;
  }

  /**
   * Loads the puzzle and associated puzzle words from the configured preload file,
   * parses them into entity objects, and persists them in the database.
   *
   * @param args command-line arguments (ignored).
   * @throws Exception if the preload file cannot be read or parsed.
   */
  @Override
  public void run(String... args) throws Exception {
    Resource puzzleData = new ClassPathResource(puzzlePreloadFile);
    try (InputStream input = puzzleData.getInputStream()) {
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      PuzzlePreloadDto puzzleDto = mapper.readValue(input, PuzzlePreloadDto.class);

      // Create and persist the puzzle entity.
      Puzzle puzzle  = new Puzzle();
      puzzle.setDate(puzzleDto.getDate());
      puzzle.setSize(puzzleDto.getSize());
      puzzle.setBoard(Puzzle.Board.valueOf(puzzleDto.getBoard().toString()));
      Puzzle updated = repository.save(puzzle);

      // Create and collect the puzzle words for the puzzle.
      List<PuzzleWord> puzzleWords = new ArrayList<>();
      for (int i = 0; i < puzzleDto.getPuzzleWords().size(); i++) {
        PuzzleWord puzzleWord = new PuzzleWord();
        puzzleWord.setPuzzle(updated);
        puzzleWord.setWordName(puzzleDto.getPuzzleWords().get(i).getWordName());
        puzzleWord.setClue(puzzleDto.getPuzzleWords().get(i).getClue());
        puzzleWord.setWordDirection(PuzzleWord.Direction.valueOf(
            puzzleDto.getPuzzleWords().get(i).getDirection().toString()));
        puzzleWord.setWordPosition(new WordPosition(
            puzzleDto.getPuzzleWords().get(i).getWordPosition().getRow(),
            puzzleDto.getPuzzleWords().get(i).getWordPosition().getColumn(),
            puzzleDto.getPuzzleWords().get(i).getWordPosition().getLength()
        ));
        puzzleWords.add(puzzleWord);
      }

      // Persist all puzzle words in batch.
      puzzleWordRepository.saveAll(puzzleWords);
    }
  }

}
