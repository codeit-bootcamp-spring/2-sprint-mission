package com.sprint.mission.discodeit.DTO.Message;

import java.util.List;
import java.util.UUID;

public record CreateMessageDto(
        String content,
        UUID channelId,
        UUID authorId,
        List<UUID> attachmentIds
) {}
