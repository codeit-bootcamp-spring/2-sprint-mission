package com.sprint.mission.discodeit.dto.readStatus;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReadStatusCreate {
    private UUID userID;
    private UUID channelID;
    private Instant lastRead;
}
