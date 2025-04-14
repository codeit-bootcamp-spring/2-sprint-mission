package com.sprint.discodeit.sprint.domain.dto.readStatusDto;

import java.util.UUID;

public record ReadStatusRequestDto(Long channelId, Long messageId, Long usersId, Boolean check) {
}
