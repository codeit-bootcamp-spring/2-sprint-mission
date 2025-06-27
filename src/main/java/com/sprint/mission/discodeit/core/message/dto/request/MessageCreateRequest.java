package com.sprint.mission.discodeit.core.message.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MessageCreateRequest(
    String content,
    @NotNull UUID channelId,
    @NotNull UUID authorId
) {

}
