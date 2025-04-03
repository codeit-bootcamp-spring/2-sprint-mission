package com.sprint.mission.discodeit.dto;

import java.time.Instant;

public record UpdateUserStatusRequestDto(
    Instant loginTime
) {

}
