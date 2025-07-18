package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.PuzzleWord;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface PuzzleWordRepository extends CrudRepository <PuzzleWord, Long> {

  Optional<PuzzleWord> findById(long id);

  Optional<PuzzleWord> findBywordName(String wordName);

  Optional<PuzzleWord> findBywordRowAndwordColumn(int wordRow, int wordColumn);

}
