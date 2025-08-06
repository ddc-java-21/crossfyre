package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.entity.User;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

/**
 * Converts a JWT (JSON Web Token) into a {@link UsernamePasswordAuthenticationToken} for use in Spring Security.
 * <p>
 * Extracts user details from the JWT claims, creates or retrieves the corresponding {@link User} entity,
 * and assigns a default authority role of "ROLE_USER".
 * </p>
 */
@Service
@Profile("service")
public class JwtConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

  private final AbstractUserService userService;

  /**
   * Constructs a {@code JwtConverter} with the given {@link AbstractUserService}.
   *
   * @param userService service used to load or create user entities based on OAuth keys
   */
  @Autowired
  JwtConverter(AbstractUserService userService) {
    this.userService = userService;
  }

  /**
   * Converts a JWT token to a Spring Security authentication token.
   * <p>
   * Extracts the user's display name, avatar URL, and OAuth key from the JWT claims,
   * ensures the user exists via the user service, and grants the user the "ROLE_USER" authority.
   * </p>
   *
   * @param source the JWT token to convert
   * @return a {@link UsernamePasswordAuthenticationToken} representing the authenticated user
   */
  @Override
  public UsernamePasswordAuthenticationToken convert(Jwt source) {
    Collection<SimpleGrantedAuthority> grants =
        Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    User user = new User();
    user.setDisplayName(source.getClaimAsString("name"));
    user.setAvatar(source.getClaimAsURL("picture"));
    user.setOauthKey(source.getSubject());
    user = userService.getOrAddUser(source.getSubject(), user);
    return new UsernamePasswordAuthenticationToken(user, source.getTokenValue(), grants);
  }
}
