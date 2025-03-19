package com.sprint.mission.discodeit.service.dto;

import com.sprint.mission.discodeit.entity.BinaryContentType;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Data
public class UserUpdateParam {
    private final UUID id;
    private final String newUsername;
    private final String newEemail;
    private final String newPassword;

    private BinaryContentType newType;
    private MultipartFile newFile;
}