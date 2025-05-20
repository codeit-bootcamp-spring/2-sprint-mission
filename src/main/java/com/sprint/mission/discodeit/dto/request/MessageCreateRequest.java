package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MessageCreateRequest(
    String content,

    @NotNull(message = "채널을 반드시 입력해야 합니다.")
    UUID channelId,

    UUID authorId
) {

}
