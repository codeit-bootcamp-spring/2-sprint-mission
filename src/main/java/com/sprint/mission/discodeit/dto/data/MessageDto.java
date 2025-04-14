package com.sprint.mission.discodeit.dto.data;

import java.util.List;
import java.util.UUID;
import java.time.Instant;

public record MessageDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UUID channelId,
    UserDto author,
    List<BinaryContentDto> attachments
) {

}
