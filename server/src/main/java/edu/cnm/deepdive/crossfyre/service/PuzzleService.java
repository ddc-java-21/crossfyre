package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("service")
public class PuzzleService implements AbstractPuzzleService {

  private final PuzzleRepository repository;

  @Autowired
  PuzzleService(PuzzleRepository repository) {
    this.repository = repository;
  }

  @Override
  public Iterable<UserPuzzle> getAll() {
    return repository.getAllByOrderByTitleAsc();
  }

  @Override
  public UserPuzzle get(UUID key) {
    return repository
        .findByExternalKey(key)
        .orElseThrow();
  }

}
