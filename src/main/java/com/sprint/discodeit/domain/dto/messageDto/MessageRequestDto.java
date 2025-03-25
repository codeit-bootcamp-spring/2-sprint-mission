package com.sprint.discodeit.domain.dto.messageDto;

import com.sprint.discodeit.domain.entity.BinaryContent;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record MessageRequestDto(String content, UUID channelId, UUID authorId, List<BinaryContent> file) {
}
