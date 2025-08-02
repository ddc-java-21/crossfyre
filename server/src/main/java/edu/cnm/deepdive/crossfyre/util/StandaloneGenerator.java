package edu.cnm.deepdive.crossfyre.util;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle.Board;
import edu.cnm.deepdive.crossfyre.service.AbstractGeneratorService;
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
@Profile({"generate", "cronjob"})
public class StandaloneGenerator implements CommandLineRunner {

  private static final String WORDS_FILE = "crossword/common-english-words.txt";

  private final AbstractGeneratorService generatorService;

  public StandaloneGenerator(AbstractGeneratorService generatorService) throws IOException {
    this.generatorService = generatorService;
  }

  @Override
  public void run(String... args) throws Exception {

    System.out.println(generatorService.generatePuzzleWords(Board.TUESDAY));
  }


}
