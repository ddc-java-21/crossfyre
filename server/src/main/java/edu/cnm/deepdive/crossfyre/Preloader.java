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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Profile("preload")
public class Preloader implements CommandLineRunner {

  private static final String DEFINITION_URL = "https://www.dictionaryapi.com/api/v3/references/collegiate/json/voluminous?key=";

  private final PuzzleRepository repository;
  private final PuzzleWordRepository puzzleWordRepository;
  private final String puzzlePreloadFile;
  private final String wordsPreloadFile;

  @Autowired
  Preloader(PuzzleRepository repository, PuzzleWordRepository puzzleWordRepository,
      @Value("${crossfyre.preload.file.puzzle}") String puzzlePreloadFile,
      @Value("${crossfyre.preload.file.words}") String wordsPreloadFile) {
    this.repository = repository;
    this.puzzleWordRepository = puzzleWordRepository;
    this.puzzlePreloadFile = puzzlePreloadFile;
    this.wordsPreloadFile = wordsPreloadFile;
  }

  @Override
  public void run(String... args) throws Exception {
    Resource puzzleData = new ClassPathResource(puzzlePreloadFile);
    try (InputStream input = puzzleData.getInputStream()) {
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      PuzzlePreloadDto puzzleDto = mapper.readValue(input, PuzzlePreloadDto.class);

      Puzzle puzzle  = new Puzzle();
      puzzle.setDate(puzzleDto.getDate());
      puzzle.setSize(puzzleDto.getSize());
      puzzle.setBoard(Puzzle.Board.valueOf(puzzleDto.getBoard().toString()));
      Puzzle updated = repository.save(puzzle);

      List<PuzzleWord> puzzleWords = new ArrayList<>();
      for (int i = 0; i < puzzleDto.getPuzzleWords().size(); i++) {
        PuzzleWord puzzleWord = new PuzzleWord();
        puzzleWord.setPuzzle(updated);
        puzzleWord.setWordName(puzzleDto.getPuzzleWords().get(i).getWordName());
        puzzleWord.setClue(puzzleDto.getPuzzleWords().get(i).getClue());
        puzzleWord.setWordDirection(PuzzleWord.Direction.valueOf(puzzleDto.getPuzzleWords().get(i).getDirection().toString()));
        puzzleWord.setWordPosition(new WordPosition(
            puzzleDto.getPuzzleWords().get(i).getWordPosition().getRow(),
            puzzleDto.getPuzzleWords().get(i).getWordPosition().getColumn(),
            puzzleDto.getPuzzleWords().get(i).getWordPosition().getLength()
        ));
        puzzleWords.add(puzzleWord);
      }

      // Merriam Webster Call
//      RestTemplate template = new RestTemplate();
//      ResponseEntity<Definition> response = template.getForEntity(ZEN_QUOTE_URL, Definition.class);
//      ZenQuote[] quotes;
//      if (
//          response.getStatusCode().is2xxSuccessful()
//              && (quotes = response.getBody()) != null
//              && quotes.length > 0
//      ) {
//        ZenQuote quote = quotes[0];
//      }

      puzzleWordRepository.saveAll(puzzleWords);

//      repository.save(puzzle);

//      PuzzleWord[] puzzleWords = mapper.readValue(input2, PuzzleWord[].class);
//      System.out.println("Preloaded puzzleWords: ");
//      Arrays.stream(puzzleWords).forEach((puzzleWord) -> {
//        System.out.println(puzzleWord.getExternalKey());
//        System.out.println(puzzleWord.getWordName());
//        System.out.println(puzzleWord.getClue());
//        System.out.println(puzzleWord.getExternalKey());
//      });
//      for (PuzzleWord puzzleWord : puzzleWords) {
//        puzzleWord.setPuzzle(puzzle);
//      }
//      puzzleWordRepository.saveAll(Arrays.asList(puzzleWords));
    }
  }

}
