package com.sprint.discodeit.domain.dto.readStatusDto;

import java.util.UUID;

public record ReadStatusRequestDto(UUID channelId, UUID messageId, UUID userId, Boolean check) {
}
