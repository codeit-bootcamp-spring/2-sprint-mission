package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record MessageCreateRequest(
    UUID channelId,
    UUID authorId,
    @NotBlank(message = "메세지를 입력해주세요")
    String content
) {

}
