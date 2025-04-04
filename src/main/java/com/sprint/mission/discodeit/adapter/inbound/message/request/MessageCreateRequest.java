package com.sprint.mission.discodeit.adapter.inbound.message.request;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record MessageCreateRequest(
    String content,
    @NotBlank UUID channelId,
    @NotBlank UUID authorId
) {

}
