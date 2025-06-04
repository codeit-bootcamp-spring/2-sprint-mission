package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MessageCreateRequest(
    String content,
    @NotNull(message = "channelId는 필수 값입니다.")
    UUID channelId,
    @NotNull(message = "channelId는 필수 값입니다.")
    UUID authorId
) {

}
