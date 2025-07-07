package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre.model.entity.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Long> {


}
