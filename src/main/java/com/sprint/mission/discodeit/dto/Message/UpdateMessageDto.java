package com.sprint.mission.discodeit.dto.Message;

import java.util.UUID;

public record UpdateMessageDto(
        UUID messageId,
        String newContent
) {}
