package com.sprint.mission.discodeit.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record UserCreateWithFileRequest(
        UserCreateRequest userCreateRequest,
        MultipartFile profileImage
) {
    public static UserCreateWithFileRequest of(UserCreateRequest request, MultipartFile profileImage) {
        return new UserCreateWithFileRequest(request, profileImage);
    }
}
