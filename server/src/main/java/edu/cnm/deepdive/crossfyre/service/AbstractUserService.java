package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.User;
import java.util.UUID;

public interface AbstractUserService {

  User getOrAddUser(String oauthKey, User profile);

  User getCurrentUser();

  User getUser(User requestor, UUID key);

  User getMe(User requestor);

  User updateMe(User requestor, User delta);

}
