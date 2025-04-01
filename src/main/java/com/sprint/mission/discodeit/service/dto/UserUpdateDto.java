package com.sprint.mission.discodeit.service.dto;

import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.entity.UserStatusType;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record UserUpdateDto(
        UUID id,
        String password,
        String nickname,
        UserStatusType status,
        UserRole role,
        UUID profileId,
        MultipartFile profileImage
) {
}
