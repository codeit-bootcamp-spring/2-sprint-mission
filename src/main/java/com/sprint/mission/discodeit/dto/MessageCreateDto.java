package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record MessageCreateDto(
        @NotNull UUID id,
        @NotNull String content,
        @NotNull UUID channelId,
        @NotNull UUID authorId,
        @Nullable String uploadFileName,
        @Nullable String storeFileName

) {
}
