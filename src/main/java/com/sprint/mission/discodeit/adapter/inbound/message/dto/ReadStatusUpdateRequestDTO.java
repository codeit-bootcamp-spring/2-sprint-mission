package com.sprint.mission.discodeit.adapter.inbound.message.dto;

import java.time.Instant;

public record ReadStatusUpdateRequestDTO(
    Instant newLastReadAt
) {

}
