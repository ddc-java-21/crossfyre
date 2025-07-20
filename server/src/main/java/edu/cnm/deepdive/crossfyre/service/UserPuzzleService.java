package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import edu.cnm.deepdive.crossfyre.service.dao.UserPuzzleRepository;
import java.time.Instant;
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

  public UserPuzzle getUserPuzzle(User user,  Instant date) {
    return userPuzzleRepository
        .findByUserAndPuzzleDate(user, date)
        .orElseThrow();
  }

}
