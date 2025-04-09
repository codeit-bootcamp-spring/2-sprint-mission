package com.sprint.mission.discodeit.dto.service.message;

import com.sprint.mission.discodeit.dto.service.user.UserDTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UpdateMessageDTO(
    UUID id,
    Instant updatedAt,
    List<UUID> attachmentIds,
    String content,
    UUID channelId,
    UUID authorId
) {

}
