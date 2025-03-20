package com.sprint.mission.discodeit.dto.message.response;

import java.util.List;
import java.util.UUID;

public record MessageCreateResponse(
        UUID id,
        UUID channelId,
        UUID authorId,
        String content,
        List<UUID> attachmentIds
) {}
