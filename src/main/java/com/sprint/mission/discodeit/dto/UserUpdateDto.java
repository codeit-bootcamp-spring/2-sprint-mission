package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

public record UserUpdateDto(
        UUID id,

        @Nullable
        String userName,

        @Nullable
        String password,

        @Nullable
        @Email
        String userEmail,

        @Nullable String uploadFileName,
        @Nullable String storeFileName

) {
}
