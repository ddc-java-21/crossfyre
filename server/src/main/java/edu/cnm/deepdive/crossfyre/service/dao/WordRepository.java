package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.Word;
import edu.cnm.deepdive.crossfyre.model.projection.Posted;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface WordRepository extends CrudRepository<Word, Long> {

  Optional<Word> findByExternalKey(UUID externalKey);

  Optional<Word> findByPuzzleAndExternalKey(Puzzle puzzle, UUID externalKey);

  Iterable<Word> findByPuzzleOrderByPostedAsc(Puzzle puzzle);

//  This is included to show a JPQL implementation of a Spring Data inferred query.
//  @Query("SELECT m FROM Message AS m WHERE m.Puzzle = :Puzzle AND m.posted > :cutoff")
  Iterable<Word> findByPuzzleAndPostedAfterOrderByPostedAsc(Puzzle puzzle, Instant cutoff);

  Optional<Posted> findFirstByPuzzleAndPostedAfterOrderByPostedDesc(Puzzle puzzle, Instant cutoff);

}
