package com.sprint.mission.discodeit.domain.user.dto;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentResult;
import com.sprint.mission.discodeit.domain.user.entity.User;

import java.time.Instant;
import java.util.UUID;

public record UserResult(UUID id,
                         Instant createdAt,
                         Instant updatedAt,
                         String username,
                         String email,
                         BinaryContentResult profile,
                         boolean online) {

    public static UserResult fromEntity(User user, boolean isOnline) {
        BinaryContentResult binaryContentResult = getBinaryContentResult(user);

        return new UserResult(user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getName(),
                user.getEmail(),
                binaryContentResult,
                isOnline);
    }

    private static BinaryContentResult getBinaryContentResult(User user) {
        if (user.getBinaryContent() == null) {
            return null;
        }

        return BinaryContentResult.fromEntity(user.getBinaryContent());
    }

}
