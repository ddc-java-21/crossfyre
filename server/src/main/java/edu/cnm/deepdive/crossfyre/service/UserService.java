package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.service.dao.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service layer for managing {@link User} entities.
 *
 * <p>Provides methods to get, add, update users and retrieve
 * information about the currently authenticated user.</p>
 */
@Service
@Profile("service")
public class UserService implements AbstractUserService {

  private final UserRepository repository;

  /**
   * Constructs a {@code UserService} with the specified repository.
   *
   * @param repository repository used to access {@link User} data.
   */
  @Autowired
  UserService(UserRepository repository) {
    this.repository = repository;
  }

  /**
   * Returns the existing user associated with the given OAuth key, or adds
   * a new user if none exists with that key.
   *
   * @param oauthKey the OAuth key identifying the user.
   * @param profile  a {@link User} object containing profile data to initialize a new user if needed.
   * @return the existing or newly created {@link User}.
   * @throws java.util.NoSuchElementException if the user cannot be retrieved or created.
   */
  @Override
  public synchronized User getOrAddUser(String oauthKey, User profile) {
    return repository
        .findByOauthKey(oauthKey)
        .or(() -> {
          User user = new User();
          user.setOauthKey(oauthKey);
          user.setDisplayName(profile.getDisplayName());
          user.setAvatar(profile.getAvatar());
          repository.save(user);
          return Optional.of(user);
        })
        .orElseThrow();
  }

  /**
   * Retrieves the currently authenticated user from the Spring Security context.
   *
   * @return the current authenticated {@link User}.
   * @throws ClassCastException if the authentication principal is not a {@code User}.
   */
  @Override
  public User getCurrentUser() {
    return (User) SecurityContextHolder.getContext()
        .getAuthentication()
        .getPrincipal();
  }

  /**
   * Retrieves a user by their external UUID key.
   *
   * @param requestor the user making the request.
   * @param key       the external UUID key of the user to retrieve.
   * @return the {@link User} matching the specified key.
   * @throws java.util.NoSuchElementException if no user is found.
   */
  @Override
  public User getUser(User requestor, UUID key) {
    return repository
        .findByExternalKey(key)
        .orElseThrow();
  }

  /**
   * Returns the user who is making the request.
   *
   * @param requestor the user making the request.
   * @return the same {@link User} instance passed in.
   */
  @Override
  public User getMe(User requestor) {
    return requestor;
  }

  /**
   * Updates the profile of the requesting user with new values provided in {@code delta}.
   * Only non-null values in {@code delta} are applied.
   *
   * @param requestor the user whose profile is to be updated.
   * @param delta     a {@link User} containing updated fields.
   * @return the updated {@link User}.
   * @throws java.util.NoSuchElementException if the user cannot be found in the repository.
   */
  @Override
  public User updateMe(User requestor, User delta) {
    return repository
        .findById(requestor.getId())
        .map((retrieved) -> {
          if (delta.getDisplayName() != null) {
            retrieved.setDisplayName(delta.getDisplayName());
          }
          if (delta.getAvatar() != null) {
            retrieved.setAvatar(delta.getAvatar());
          }
          return repository.save(retrieved);
        })
        .orElseThrow();
  }
}
