package com.sprint.discodeit.sprint.domain.dto.messageDto;

import java.util.List;

public record MessageRequestDto(String content, Long userId, List<String> file) {
}
