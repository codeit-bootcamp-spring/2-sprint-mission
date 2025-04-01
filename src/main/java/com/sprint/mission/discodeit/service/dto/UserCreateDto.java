package com.sprint.mission.discodeit.service.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.entity.UserStatusType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
        MultipartFile profileImage,

        //UserStatus
        UUID userId,
        Instant lastActiveAt
) {
    public static BinaryContent fromMultipartFile(MultipartFile file, String filePath) throws IOException {
        return new BinaryContent(
                file.getBytes(),
                file.getOriginalFilename(),
                filePath,
                file.getContentType(),
//                getFileExtension(file.getOriginalFilename()),
                file.getSize()
        );
    }

    public User convertDtoToUser(UUID profileId) {
        return new User(email, password, nickname, status, role, profileId);
    }

}
