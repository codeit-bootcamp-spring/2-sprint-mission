package com.sprint.mission.discodeit.DTO.Message;

import java.util.UUID;

public record UpdateMessageDto(
        UUID messageId,
        String newContent
) {}
