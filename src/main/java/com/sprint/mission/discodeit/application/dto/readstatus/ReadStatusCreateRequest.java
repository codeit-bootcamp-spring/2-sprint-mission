package com.sprint.mission.discodeit.application.dto.readstatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "읽음 상태 생성 요청")
public record ReadStatusCreateRequest(
        @NotNull UUID userId,
        @NotNull UUID channelId) {

}
