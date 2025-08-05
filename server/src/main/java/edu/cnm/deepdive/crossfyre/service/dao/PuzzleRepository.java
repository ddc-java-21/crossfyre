package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface PuzzleRepository extends CrudRepository<Puzzle, Long> {

  Optional<Puzzle> findById(long id);

  Optional<Puzzle> findByExternalKey(UUID externalKey);

  Optional<Puzzle> findByDate(LocalDate date);

}
