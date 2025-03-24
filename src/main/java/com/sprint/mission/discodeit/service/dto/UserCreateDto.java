package com.sprint.mission.discodeit.service.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.entity.UserStatusType;

import java.time.Instant;
import java.util.UUID;

public record UserCreateDto(
        //User
        String email,
        String password,
        String nickname,
        UserStatusType status,
        UserRole role,
        UUID profileId,
        //BinaryContent
        byte[] fileData,
        String filePath,
        String fileName,
        String fileType,
        long fileSize,
        //UserStatus
        UUID userId,
        Instant lastActiveAt
) {
    public BinaryContent convertDtoToBinaryContent() {
        return new BinaryContent(fileData, filePath, fileName, fileType, fileSize);
    }

    public User convertDtoToUser(UUID profileId) {
        return new User(email, password, nickname, status, role, profileId);
    }

    public static UserCreateDto withoutProfile(String email, String password, String nickname, UserStatusType status, UserRole role) {
        return new UserCreateDto(email, password, nickname, status, role, null, null, null, null, null, 0L, null, null);
    }
}
