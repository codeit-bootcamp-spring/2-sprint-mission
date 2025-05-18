package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MessageCreateRequest(
    @NotNull
    String content,
    @NotNull
    UUID channelId,
    @NotNull
    UUID authorId
) {

}
