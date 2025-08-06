package edu.cnm.deepdive.crossfyre.controller;

import edu.cnm.deepdive.crossfyre.model.entity.User;
import edu.cnm.deepdive.crossfyre.service.AbstractUserService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles HTTP requests related to {@link User} entities. Provides endpoints for retrieving and
 * updating user information, particularly for the currently authenticated user.
 *
 * <p>This controller is active only when the {@code service} Spring profile is enabled.</p>
 */
@RestController
@RequestMapping("/users")
@Validated
@Profile("service")
public class UserController {

  private final AbstractUserService service;

  /**
   * Initializes this controller with the injected {@link AbstractUserService}.
   *
   * @param service the user service used to handle user-related operations.
   */
  @Autowired
  UserController(AbstractUserService service) {
    this.service = service;
  }

  /**
   * Retrieves the profile of the currently authenticated user.
   *
   * @return the {@link User} entity representing the authenticated user.
   */
  @GetMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
  public User get() {
    return service.getMe(service.getCurrentUser());
  }

  /**
   * Retrieves the user with the specified {@link UUID}, subject to the access permissions of the
   * currently authenticated user.
   *
   * @param key the UUID of the user to retrieve.
   * @return the {@link User} entity with the given key.
   */
  @GetMapping(path = "/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
  public User get(@PathVariable UUID key) {
    return service.getUser(service.getCurrentUser(), key);
  }

  /**
   * Updates the profile of the currently authenticated user with the values provided in the request
   * body.
   *
   * @param delta a {@link User} entity containing updated values.
   * @return the updated {@link User} entity after applying changes.
   */
  @PutMapping(path = "/me",
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public User updateMe(@Valid @RequestBody User delta) {
    return service.updateMe(service.getCurrentUser(), delta);
  }
}
