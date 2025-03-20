package com.sprint.mission.discodeit.dto.message.common;

import com.sprint.mission.discodeit.domain.BinaryContent;

import java.util.List;
import java.util.UUID;

public record MessageCreateDto(
        String content,
        UUID channelId,
        UUID authorId,
        List<BinaryContent> binaryContents
) {
    public MessageCreateDto(String content, UUID channelId, UUID authorId) {
        this(content, channelId, authorId, List.of());
    }
}
