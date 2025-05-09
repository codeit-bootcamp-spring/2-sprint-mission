package com.sprint.mission.discodeit.dto.service.message;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.UUID;

public record MessageCreateRequest(
    @NotEmpty String content,
    @NotBlank UUID channelId,
    @NotBlank UUID authorId
) {

}