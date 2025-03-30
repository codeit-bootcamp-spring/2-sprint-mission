package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record CreateMessageDTO(
        String text,
        UUID userId,
        UUID channelId,
        Optional<List<BinaryContentDTO>>attachments
) {
}
