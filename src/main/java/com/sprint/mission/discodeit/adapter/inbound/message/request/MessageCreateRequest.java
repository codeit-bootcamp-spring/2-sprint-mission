package com.sprint.mission.discodeit.adapter.inbound.message.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MessageCreateRequest(
    String content,
    @NotNull UUID channelId,
    @NotNull UUID authorId
) {

}
