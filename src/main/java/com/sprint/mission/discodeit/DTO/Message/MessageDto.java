package com.sprint.mission.discodeit.DTO.Message;

import java.util.List;
import java.util.UUID;

public record MessageDto(
        UUID id,
        String content,
        UUID channelId,
        UUID authorId,
        List<UUID> attachmentIds
) {}
