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

@Service
@Profile("service")
public class JwtConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

  private final AbstractUserService userService;

  @Autowired
  JwtConverter(AbstractUserService userService) {
    this.userService = userService;
  }

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
