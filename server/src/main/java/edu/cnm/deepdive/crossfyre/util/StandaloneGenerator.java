package edu.cnm.deepdive.crossfyre.util;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle.Board;
import edu.cnm.deepdive.crossfyre.service.AbstractGeneratorService;
import java.io.IOException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("generate")
public class StandaloneGenerator implements CommandLineRunner {

  private static final String WORDS_FILE = "crossword/common-english-words.txt";

  private final AbstractGeneratorService generatorService;

  public StandaloneGenerator(AbstractGeneratorService generatorService) throws IOException {
    this.generatorService = generatorService;
  }
  @Override
  public void run(String... args) throws Exception {

    System.out.println(generatorService.generatePuzzleWords(Board.WEDNESDAY));
  }
}
