package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.UserPuzzle;
import edu.cnm.deepdive.crossfyre.service.dao.UserPuzzleRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("service")
public class UserPuzzleService implements AbstractUserPuzzleService {

  private final UserPuzzleRepository userPuzzleRepository;


  public UserPuzzleService(UserPuzzleRepository userPuzzleRepository) {
    this.userPuzzleRepository = userPuzzleRepository;
  }

  @Override
  public UserPuzzle getOrAddUserPuzzle()

}
