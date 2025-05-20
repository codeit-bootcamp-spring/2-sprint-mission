package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MessageCreateRequest(
    @NotBlank(message = "Message 내용 입력 필수")
    String content,

    @NotNull(message = "ChannelId 입력 필수")
    UUID channelId,

    @NotNull(message = "UserId 입력 필수")
    UUID authorId
) {

}
