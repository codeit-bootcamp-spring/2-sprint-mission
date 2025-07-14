package com.sprint.mission.discodeit.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(
    @NotBlank @Size(min = 32, message = "JWT secret must be at least 32 characters long")
    String secret,

    @NotNull @Positive(message = "Access token expiration must be positive")
    Long accessTokenExpiration,

    @NotNull @Positive(message = "Refresh token expiration must be positive")
    Long refreshTokenExpiration,

    @NotBlank(message = "JWT issuer cannot be blank")
    String issuer
) {

}
