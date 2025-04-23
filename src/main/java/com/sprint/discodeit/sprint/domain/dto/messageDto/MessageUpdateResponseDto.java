package com.sprint.discodeit.sprint.domain.dto.messageDto;

import java.time.Instant;

public record MessageUpdateResponseDto(Instant updateAt, String content) {
}
