package com.sprint.mission.discodeit.dto.service.jwt;

public record RefreshResult(
    String accessToken,
    String refreshToken
) {

}
