package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "수정할 Channel 정보")
public record PublicChannelUpdateRequest(
    @NotBlank(message = "newName은 공백일 수 없습니다.")
    String newName,
    String newDescription
) {

}
