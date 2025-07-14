package com.sprint.mission.discodeit.security.jwt;

public record JwtDto(
    String accessToken,
    String refreshToken
) {

}
