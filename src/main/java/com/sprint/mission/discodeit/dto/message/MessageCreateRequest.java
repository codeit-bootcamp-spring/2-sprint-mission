package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record MessageCreateRequest(
    String content,
    UUID channelId,
    UUID authorId
) {

}
