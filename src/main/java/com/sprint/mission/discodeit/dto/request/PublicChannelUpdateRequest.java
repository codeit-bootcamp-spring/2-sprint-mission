package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "공개 채널 업데이트 요청")
public record PublicChannelUpdateRequest(
        @Size(min = 1, max = 100, message = "채널명은 1~100자 사이여야 합니다.")
        String newName,

        @Size(max = 255, message = "설명은 255자 이하로 입력해야 합니다.")
        String newDescription
) {
}
