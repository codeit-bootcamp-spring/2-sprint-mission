package com.sprint.mission.discodeit.dto.message.common;

import java.util.List;
import java.util.UUID;

public record MessageUpdateDto(
        UUID id,
        String content,
        List<UUID> attachmentIds
) {
    public MessageUpdateDto(UUID id, String content) {
        this(id, content, List.of());
    }
}
