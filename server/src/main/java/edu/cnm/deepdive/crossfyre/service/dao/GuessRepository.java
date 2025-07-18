package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.Guess;
import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface GuessRepository extends CrudRepository<Guess, Character> {

  Optional<Guess> findById(long id);

  List<Guess> findAllByUserPuzzle(UserPuzzle userPuzzle);

  List<Guess> findByUserPuzzleOrderByCreatedAsc(UserPuzzle puzzle);

  Optional<Guess> findByguessPosition_GuessRowAndguessPosition_guessColumn(int wordRow, int wordColumn);
//
//  This is included to show a JPQL implementation of a Spring Data inferred query.
//  @Query("SELECT m FROM Message AS m WHERE m.UserPuzzle = :UserPuzzle AND m.posted > :cutoff")
//  Iterable<Guess> findByUserPuzzleAndCreatedAfterOrderByCreatedAsc(UserPuzzle puzzle, Instant cutoff);
//
//  Optional<Guess> findFirstByUserPuzzleAndCreatedAfterOrderByCreatedDesc(UserPuzzle UserPuzzle, Instant cutoff);

}
