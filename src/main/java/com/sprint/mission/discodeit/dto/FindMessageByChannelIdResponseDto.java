package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record FindMessageByChannelIdResponseDto(
        UUID messageId,
        String nickname,
        List<UUID> attachmentList,
        String content,
        Instant createAt
) {

}
