package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import edu.cnm.deepdive.crossfyre.service.dao.PuzzleRepository;
import edu.cnm.deepdive.crossfyre.service.dao.UserPuzzleRepository;
import edu.cnm.deepdive.crossfyre.service.dao.UserRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("service")
public class UserPuzzleService implements AbstractUserPuzzleService {

  private final UserPuzzleRepository userPuzzleRepository;
  private final PuzzleRepository puzzleRepository;

  private final UserRepository userRepository;

  @Autowired
  UserPuzzleService(UserPuzzleRepository userPuzzleRepository, PuzzleRepository puzzleRepository,
      UserRepository userRepository) {
    this.userPuzzleRepository = userPuzzleRepository;
    this.puzzleRepository = puzzleRepository;
    this.userRepository = userRepository;
  }

  @Override
  public UserPuzzle get(UUID userPuzzleKey) {
    return userPuzzleRepository
        .findByExternalKey(userPuzzleKey)
        .orElseThrow();
  }

  @Override
  public Iterable<UserPuzzle> getAllByPuzzle(UUID puzzleKey) {
    return puzzleRepository
        .findByExternalKey(puzzleKey)
        .map(userPuzzleRepository::findByPuzzleOrderByCreatedDesc)
        .orElseThrow();
  }

  @Override
  public Iterable<UserPuzzle> getAllByPuzzleDate(Instant date) {
    return puzzleRepository
        .findByDate(date)
        .map(userPuzzleRepository::findByPuzzleOrderByCreatedDesc)
        .orElseThrow();
  }

  @Override
  public Iterable<UserPuzzle> getAllByUser(User user) {
    return userRepository
        .findById(user.getId())
        .map(userPuzzleRepository::findByUserOrderByCreatedDesc)
        .orElseThrow();
  }

  @Override
  public UserPuzzle getOrAddUserPuzzle(User user, Instant date) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(user, date)
        .or(() -> Optional.of(
            puzzleRepository
            .findByDate(date)
            .map((puzzle) -> {
              UserPuzzle userPuzzle = new UserPuzzle();
              userPuzzle.setUser(user);
              userPuzzle.setPuzzle(puzzle);
              userPuzzle.setGuesses(new ArrayList<>());
              return userPuzzleRepository.save(userPuzzle);
            })
            .orElseThrow()
        ))
        .orElseThrow();
  }

  @Override
  public UserPuzzle get(User user, Instant date) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(user, date)
        .orElseThrow();
  }


  // TODO: 7/20/25 Add checking logic to determine solved state of both existing and delta (?)
  @Override
  public UserPuzzle updateUserPuzzle(UserPuzzle existing, UserPuzzle delta) {
    // TODO: 7/20/25 Update the UserPuzzle state as such:
    //  1. Bring in changes from delta (HTTP)
    //  2. Update list of guesses
    //  3. Run checking logic (comparison of two grids)
    //  4. Update solved and finished (time) as necessary
    return userPuzzleRepository
        .findById(existing.getId())
        .map((retrieved) -> {

          // TODO: 7/20/25 Check if puzzle is solved


          retrieved.setSolved(delta.isSolved());
          if (delta.isSolved()) {
            delta.setFinished(Instant.now());
            retrieved.setFinished(delta.getFinished());
          }
          if (delta.getGuesses() != null) {
            retrieved.setGuesses(delta.getGuesses());
          }
          return userPuzzleRepository.save(retrieved);
        })
        .orElseThrow();
  }



}
