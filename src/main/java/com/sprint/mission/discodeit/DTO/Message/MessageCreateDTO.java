package com.sprint.mission.discodeit.DTO.Message;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;

public record MessageCreateDTO(
        String creatorId,
        String channelId,
        String text,
        List<BinaryContent> binaryContent
) {
}
