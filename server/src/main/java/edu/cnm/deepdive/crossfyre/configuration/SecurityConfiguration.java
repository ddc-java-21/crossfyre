package edu.cnm.deepdive.crossfyre.configuration;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configures Spring Security for the Crossfyre service application profile.
 * Enables OAuth2 JWT resource server support with stateless session policy,
 * and ensures requests are authenticated using JWT tokens validated
 * against a known issuer and client ID (audience).
 */
@Configuration
@EnableWebSecurity
@Profile("service")
public class SecurityConfiguration {

  /**
   * Converts a {@link Jwt} into an {@link AbstractAuthenticationToken}.
   */
  private final Converter<Jwt, ? extends AbstractAuthenticationToken> converter;

  /**
   * The URI identifying the trusted issuer of JWTs.
   */
  private final String issuerUri;

  /**
   * The expected audience (client ID) in incoming JWTs.
   */
  private final String clientId;

  /**
   * Creates a {@code SecurityConfiguration} instance with required components injected.
   *
   * @param converter JWT-to-authentication-token converter.
   * @param issuerUri Issuer URI from application properties.
   * @param clientId Client ID (audience) from application properties.
   */
  @Autowired
  public SecurityConfiguration(
      Converter<Jwt, ? extends AbstractAuthenticationToken> converter,
      @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri,
      @Value("${spring.security.oauth2.resourceserver.jwt.client-id}") String clientId
  ) {
    this.converter = converter;
    this.issuerUri = issuerUri;
    this.clientId = clientId;
  }

  /**
   * Defines the Spring Security filter chain for the application.
   * Configures stateless session management, authenticates all requests,
   * and applies JWT authentication using a custom converter.
   *
   * @param security The {@link HttpSecurity} object provided by Spring Security.
   * @return A configured {@link SecurityFilterChain}.
   * @throws Exception If the filter chain cannot be built.
   */
  @Bean
  public SecurityFilterChain provideFilterChain(HttpSecurity security) throws Exception {
    return security
        .sessionManagement((configurer) ->
            configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests((auth) -> auth.anyRequest().authenticated())
        .oauth2ResourceServer((customizer) ->
            customizer.jwt((jwt) -> jwt.jwtAuthenticationConverter(converter)))
        .build();
  }

  /**
   * Configures a {@link JwtDecoder} with issuer and audience validation.
   * Ensures tokens are both issued by a trusted issuer and intended for the specified client ID.
   *
   * @return A configured {@link JwtDecoder} instance.
   */
  @Bean
  public JwtDecoder provideDecoder() {
    NimbusJwtDecoder decoder = JwtDecoders.fromIssuerLocation(issuerUri);
    OAuth2TokenValidator<Jwt> audienceValidator =
        new JwtClaimValidator<List<String>>(JwtClaimNames.AUD, (aud) -> aud.contains(clientId));
    OAuth2TokenValidator<Jwt> issuerValidator = JwtValidators.createDefaultWithIssuer(issuerUri);
    OAuth2TokenValidator<Jwt> combinedValidator =
        new DelegatingOAuth2TokenValidator<>(audienceValidator, issuerValidator);
    decoder.setJwtValidator(combinedValidator);
    return decoder;
  }

}