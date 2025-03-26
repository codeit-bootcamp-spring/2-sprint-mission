package com.sprint.mission.discodeit.dto;

import java.util.List;
import java.util.UUID;

public record SaveMessageParamDto(
        UUID channelUUID,
        UUID userUUID,
        String content,
        List<UUID> attachmentList
) {
}
