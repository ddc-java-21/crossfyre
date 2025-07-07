package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import edu.cnm.deepdive.crossfyre.model.entity.UserWord;
import org.springframework.data.repository.CrudRepository;

public interface UserWordRepository extends CrudRepository<UserWord, Long> {

  Iterable<UserWord> findByUserPuzzle(UserPuzzle userPuzzleId);

  Iterable<UserWord> findByWordName(String wordName);

  Iterable<UserWord> findByClue(String clue);

//  This is included to show a JPQL implementation of a Spring Data inferred query.
//  @Query("SELECT m FROM Message AS m WHERE m.Puzzle = :Puzzle AND m.posted > :cutoff")

}
