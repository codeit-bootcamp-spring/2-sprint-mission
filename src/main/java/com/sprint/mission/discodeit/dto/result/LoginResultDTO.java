package com.sprint.mission.discodeit.dto.result;

public record LoginResultDTO(
        boolean success,
        String token
) {
}
