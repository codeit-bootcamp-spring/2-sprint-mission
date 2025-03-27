package com.sprint.mission.discodeit.dto.readStatus;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ReadStatusUpdateRequestDto {
    private UUID readStatusId;
    private Instant newLastReadAt;
}
