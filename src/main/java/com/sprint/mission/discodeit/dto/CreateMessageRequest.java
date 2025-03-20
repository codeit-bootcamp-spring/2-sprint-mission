package com.sprint.mission.discodeit.dto;

import java.util.List;
import java.util.UUID;

public record CreateMessageRequest(
        UUID userId,
        UUID channelId,
        String text,
        List<UUID> attachmentIds
) {}
