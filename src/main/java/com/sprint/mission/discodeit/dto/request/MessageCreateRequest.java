package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Schema(description = "Message 생성 정보")
public record MessageCreateRequest(
    @NotBlank(message = "content가 공백일 수 없습니다.")
    String content,

    @NotNull(message = "channelId가 null일 수 없습니다.")
    UUID channelId,

    @NotNull(message = "authorId가 null일 수 없습니다.")
    UUID authorId
) {

}
