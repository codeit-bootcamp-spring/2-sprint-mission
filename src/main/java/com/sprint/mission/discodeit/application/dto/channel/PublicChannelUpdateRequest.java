package com.sprint.mission.discodeit.application.dto.channel;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Public 채널 수정 요청")
public record PublicChannelUpdateRequest(
        String newName,
        String newDescription) {
}
