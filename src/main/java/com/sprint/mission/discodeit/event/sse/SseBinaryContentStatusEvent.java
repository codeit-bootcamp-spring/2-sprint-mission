package com.sprint.mission.discodeit.event.sse;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import java.time.Instant;
import java.util.UUID;

public record SseBinaryContentStatusEvent(
    UUID receiverId,
    BinaryContentDto binaryContent,
    Instant timestamp
) {

}