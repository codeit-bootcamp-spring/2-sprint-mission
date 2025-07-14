package com.sprint.mission.discodeit.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
    String secret,
    Long accessTokenExpiration,
    Long refreshTokenExpiration,
    String issuer
) {

  public JwtProperties {
    if (secret == null || secret.length() < 32) {
      throw new IllegalArgumentException("JWT secret must be at least 32 characters");
    }
    if (accessTokenExpiration == null || accessTokenExpiration <= 0) {
      throw new IllegalArgumentException("Access token expiration must be positive");
    }
    if (refreshTokenExpiration == null || refreshTokenExpiration <= 0) {
      throw new IllegalArgumentException("Refresh token expiration must be positive");
    }
    if (issuer == null || issuer.isBlank()) {
      throw new IllegalArgumentException("JWT issuer cannot be blank");
    }
  }
}
