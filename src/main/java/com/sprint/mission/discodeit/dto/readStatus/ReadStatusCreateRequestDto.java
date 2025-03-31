package com.sprint.mission.discodeit.dto.readStatus;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReadStatusCreateRequestDto {
    private UUID userID;
    private UUID channelID;
    private Instant lastRead;
}
