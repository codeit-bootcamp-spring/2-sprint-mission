package com.sprint.mission.discodeit.dto.message.response;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MessageDto(
        @NotNull UUID id,
        @NotNull String content,
        @NotNull UUID channelId,
        @NotNull UUID authorId,
        @Nullable String uploadFileName,
        @Nullable String storeFileName
) {

}
