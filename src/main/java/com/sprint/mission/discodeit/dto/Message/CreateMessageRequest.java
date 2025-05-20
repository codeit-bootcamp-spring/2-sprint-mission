package com.sprint.mission.discodeit.dto.Message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateMessageRequest(

    @NotBlank
    String content,

    @NotNull
    UUID channelId,

    @NotNull
    UUID authorId
) {

}