package com.sprint.mission.discodeit.core.message.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MessageCreateRequest(
    @NotNull UUID authorId,
    @NotNull UUID channelId,
    String content
) {

}
