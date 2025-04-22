package com.sprint.mission.discodeit.dto.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Public 채널 생성 요청")
public record PublicChannelCreateRequest(
        @NotBlank String name,
        @NotBlank String description) {
}
