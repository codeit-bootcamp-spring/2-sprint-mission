package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ReadStatusResponseDTO {
    private UUID readStatusId;
    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;
}
