package com.sprint.mission.discodeit.dto.message.request;

import com.sprint.mission.discodeit.domain.BinaryContent;

import java.util.List;
import java.util.UUID;

public record MessageCreateRequest(
        UUID channelId,
        UUID authorId,
        String content,
        List<UUID> attachmentIds
) {
    public MessageCreateRequest(UUID channelId, UUID authorId, String content) {
        this(channelId, authorId, content, List.of());
    }
}
