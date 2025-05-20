package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;


public record MessageCreateRequest(
    @NotNull String content,
    UUID channelId,
    UUID authorId
) {

}
