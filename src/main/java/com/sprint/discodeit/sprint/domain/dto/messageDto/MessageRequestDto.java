package com.sprint.discodeit.sprint.domain.dto.messageDto;

import com.sprint.discodeit.sprint.domain.entity.BinaryContent;
import java.util.List;
import java.util.UUID;

public record MessageRequestDto(String content, Long userId, List<String> file) {
}
