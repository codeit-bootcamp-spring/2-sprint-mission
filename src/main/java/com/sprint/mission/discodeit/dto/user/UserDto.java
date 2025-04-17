package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;

public record UserDto(
        UUID id,
        String username,
        String email,
        BinaryContentDto profile,
        boolean online
) {

    public UserDto(User user, BinaryContentDto profile, boolean online) {
        this(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                profile,
                online
        );
    }

    public UserDto(User user, boolean online) {
        this(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                toBinaryContentDto(user.getProfile()),
                online
        );
    }

    private static BinaryContentDto toBinaryContentDto(BinaryContent profile) {
        if (profile == null) {
            return null;
        }
        return new BinaryContentDto(
                profile.getId(),
                profile.getFileName(),
                profile.getSize(),
                profile.getContentType()
        );
    }
}
