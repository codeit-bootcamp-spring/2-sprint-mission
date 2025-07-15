package com.sprint.mission.discodeit.security.jwt;

public record JwtToken(
        String accessToken,
        String refreshToken) {
} 