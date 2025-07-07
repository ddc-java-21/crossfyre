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


// TODO: 7/7/2025 Restart security and signon


@Configuration
@EnableWebSecurity
@Profile("service")
public class SecurityConfiguration {

  private final Converter<Jwt, ? extends AbstractAuthenticationToken> converter;
  private final String issuerUri;
  private final String clientId;

  @Autowired
  SecurityConfiguration(
      Converter<Jwt, ? extends AbstractAuthenticationToken> converter,
      @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri,
      @Value("${spring.security.oauth2.resourceserver.jwt.client-id}") String clientId
  ) {
    this.converter = converter;
    this.issuerUri = issuerUri;
    this.clientId = clientId;
  }

  @Bean
  SecurityFilterChain provideFilterChain(HttpSecurity security) throws Exception {
    return security
        .sessionManagement((configurer) ->
            configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests((auth) -> auth.anyRequest().authenticated())
        .oauth2ResourceServer((customizer) ->
            customizer.jwt((jwt) -> jwt.jwtAuthenticationConverter(converter)))
        .build();
  }

  @Bean
  JwtDecoder provideDecoder() {
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
