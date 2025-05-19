package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MessageCreateRequest(
    @NotBlank(message = "메시지 내용은 비어있을 수, 공백일 수 없습니다.")
    String content,

    @NotNull(message = "Channel ID는 필수 입력 값입니다.")
    UUID channelId,

    @NotNull(message = "Author ID는 필수 입력 값입니다.")
    UUID authorId
) {

}
