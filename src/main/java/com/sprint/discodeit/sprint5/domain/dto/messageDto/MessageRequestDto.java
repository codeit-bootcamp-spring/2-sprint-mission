package com.sprint.discodeit.sprint5.domain.dto.messageDto;

import com.sprint.discodeit.sprint5.domain.entity.BinaryContent;
import java.util.List;
import java.util.UUID;

public record MessageRequestDto(String content, UUID authorId, List<BinaryContent> file) {
}
