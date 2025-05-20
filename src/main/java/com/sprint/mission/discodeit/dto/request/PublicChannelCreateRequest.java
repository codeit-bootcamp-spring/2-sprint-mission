package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "공개 채널 생성 요청")
public record PublicChannelCreateRequest(
        @NotBlank(message = "채널명은 필수입니다.")
        String name,

        @Size(max = 255, message = "설명은 255자 이하로 입력해야 합니다.")
        String description
) {
}
