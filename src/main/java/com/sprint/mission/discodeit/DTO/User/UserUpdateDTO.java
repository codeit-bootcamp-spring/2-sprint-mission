package com.sprint.mission.discodeit.DTO.User;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public record UserUpdateDTO(
        UUID replaceId,
        String replaceName,
        String replaceEmail,
        UUID binaryContentId
) {
}
