package com.sprint.discodeit.domain.dto.messageDto;

import java.util.UUID;

public record MessageUpdateRequestDto(UUID messageId, String newContent) {
}
