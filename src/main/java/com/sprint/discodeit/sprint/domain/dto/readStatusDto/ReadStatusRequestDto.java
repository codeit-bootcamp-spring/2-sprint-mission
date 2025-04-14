package com.sprint.discodeit.sprint.domain.dto.readStatusDto;

import java.util.UUID;

public record ReadStatusRequestDto(UUID channelId, UUID messageId, UUID usersId, Boolean check) {
}
