package edu.cnm.deepdive.crossfyre;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.cnm.deepdive.crossfyre.model.dto.preload.PuzzlePreloadDto;
import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord.wordPosition;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleRepository;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleWordRepository;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@Profile("preload")
public class Preloader implements CommandLineRunner {

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
    System.out.println(puzzlePreloadFile);
    System.out.println(wordsPreloadFile);
    Resource puzzleData = new ClassPathResource(puzzlePreloadFile);
    Resource wordsData = new ClassPathResource(wordsPreloadFile);
    try (InputStream input = puzzleData.getInputStream(); InputStream input2 = wordsData.getInputStream()) {
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      PuzzlePreloadDto puzzleDto = mapper.readValue(input, PuzzlePreloadDto.class);
      System.out.println("Preloaded puzzle: ");
      System.out.println(puzzleDto.getExternalKey());
      System.out.println(puzzleDto.getSize());
      System.out.println(puzzleDto.getBoard());
      System.out.println(puzzleDto.getCreated());
      System.out.println(puzzleDto.getDate());
      System.out.println(puzzleDto.getPuzzleWords().getFirst().getWordPosition().getLength());

      // Set values into actual puzzle and send into the brig:



      Puzzle puzzle  = new Puzzle();
      puzzle.setDate(puzzleDto.getDate());
      puzzle.setSize(puzzleDto.getSize());
      puzzle.setBoard(Puzzle.Board.valueOf(puzzleDto.getBoard().toString()));
      Puzzle updated = repository.save(puzzle);
      System.out.println("Updated puzzle id: " + updated.getId());

      List<PuzzleWord> puzzleWords = new ArrayList<>();
      for (int i = 0; i < puzzleDto.getPuzzleWords().size(); i++) {
        PuzzleWord puzzleWord = new PuzzleWord();
        puzzleWord.setPuzzle(updated);
        puzzleWord.setWordName(puzzleDto.getPuzzleWords().get(i).getWordName());
        puzzleWord.setClue(puzzleDto.getPuzzleWords().get(i).getClue());
        puzzleWord.setWordDirection(PuzzleWord.Direction.valueOf(puzzleDto.getPuzzleWords().get(i).getDirection().toString()));
        puzzleWord.setWordPosition(new wordPosition(
            puzzleDto.getPuzzleWords().get(i).getWordPosition().getRow(),
            puzzleDto.getPuzzleWords().get(i).getWordPosition().getColumn(),
            puzzleDto.getPuzzleWords().get(i).getWordPosition().getLength()
        ));
        puzzleWords.add(puzzleWord);
      }
      for (PuzzleWord puzzleWord : puzzleWords) {
        System.out.println("Preloaded puzzleWords: ");
        System.out.println(puzzleWord.getWordName());
        System.out.println(puzzleWord.getClue());
        System.out.println(puzzleWord.getExternalKey());
        System.out.println(puzzleWord.getPuzzle().getId());
      }
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
