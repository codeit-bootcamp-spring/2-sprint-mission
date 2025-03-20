package com.sprint.mission.discodeit.service.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Data
public class UserCreateRequest {
    private final String username;
    private final String email;
    private final String password;

    private BinaryContentType type;
    private MultipartFile file;
}


