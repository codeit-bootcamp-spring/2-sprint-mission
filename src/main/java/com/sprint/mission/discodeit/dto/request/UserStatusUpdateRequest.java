package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

@Schema(description = "변경할 User 온라인 상태 정보")
public record UserStatusUpdateRequest(
    @NotNull(message = "newLastActiveAt에 null값이 들어올 수 없습니다.")
    Instant newLastActiveAt
) {

}
