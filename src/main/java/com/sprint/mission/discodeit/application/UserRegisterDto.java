package com.sprint.mission.discodeit.application;

import jakarta.annotation.Nullable;
import org.springframework.web.multipart.MultipartFile;

public record UserRegisterDto(String name, String email, String password, @Nullable MultipartFile multipartFile) {
}
