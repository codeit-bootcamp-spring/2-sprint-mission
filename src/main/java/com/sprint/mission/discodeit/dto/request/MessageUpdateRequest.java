package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "수정할 Message 내용")
public record MessageUpdateRequest(
    @NotNull(message = "newContent가 null일 수 없습니다.")
    String newContent
) {

}
