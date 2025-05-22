package com.sprint.mission.discodeit.dto.service.message;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MessageCreateRequest(
    @NotEmpty String content,
    @NotNull UUID channelId,
    @NotNull UUID authorId
) {

}