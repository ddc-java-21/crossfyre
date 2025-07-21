package edu.cnm.deepdive.crossfyre;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleRepository;
import java.io.InputStream;
import java.util.Arrays;
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
  private final String preloadFile;

  @Autowired
  Preloader(PuzzleRepository repository,
      @Value("${crossfyre.preload.file}") String preloadFile) {
    this.repository = repository;
    this.preloadFile = preloadFile;
  }

  @Override
  public void run(String... args) throws Exception {
    Resource puzzleData = new ClassPathResource(preloadFile);
    try (InputStream input = puzzleData.getInputStream()) {
      ObjectMapper mapper = new ObjectMapper();
      Puzzle[] puzzles = mapper.readValue(input, Puzzle[].class);
      repository.saveAll(Arrays.asList(puzzles));
    }
  }

}
