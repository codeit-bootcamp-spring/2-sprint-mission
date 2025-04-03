package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "공개 채널 업데이트 요청")
public record PublicChannelUpdateRequest(
        String newName,
        String newDescription
) {
}
