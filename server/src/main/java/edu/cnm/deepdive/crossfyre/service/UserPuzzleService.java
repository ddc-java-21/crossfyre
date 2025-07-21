package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.Puzzle;
import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import edu.cnm.deepdive.crossfyre.service.dao.UserPuzzleRepository;
import java.time.Instant;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("service")
public class UserPuzzleService implements AbstractUserPuzzleService {

  private final UserPuzzleRepository userPuzzleRepository;

  @Autowired
  public UserPuzzleService(UserPuzzleRepository userPuzzleRepository) {
    this.userPuzzleRepository = userPuzzleRepository;
  }

  @Override
  public Iterable<UserPuzzle> getAll(User user, Puzzle puzzle) {
    return userPuzzleRepository.findByUserAndPuzzleOrderByCreatedAsc(user, puzzle);
  }

  @Override
  public UserPuzzle getOrAddUserPuzzle(User user, Puzzle puzzle) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(user, puzzle.getPuzzleDate())
        .or(() -> {
          UserPuzzle userPuzzle = new UserPuzzle();
          userPuzzle.setUser(user);
          userPuzzle.setSolutionPuzzle(puzzle);
//          userPuzzle.setIsSolved(puzzle.get)
          userPuzzleRepository.save(userPuzzle);
          return Optional.of(userPuzzle);
        })
        .orElseThrow();
  }

  @Override
  public UserPuzzle get(User user,  Instant date) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(user, date)
        .orElseThrow();
  }

}
