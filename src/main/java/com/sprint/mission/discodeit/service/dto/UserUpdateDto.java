package com.sprint.mission.discodeit.service.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.entity.UserStatusType;

import java.util.UUID;

public record UserUpdateDto(
        UUID id,
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
        long fileSize
) {
    public BinaryContent convertDtoToBinaryContent() {
        return new BinaryContent(fileData, filePath, fileName, fileType, fileSize);
    }

    public static UserUpdateDto withoutProfile(UUID id, String password, String nickname, UserStatusType status, UserRole role) {
        return new UserUpdateDto(id, password, nickname, status, role, null, null, null, null, null, 0L);
    }
}
