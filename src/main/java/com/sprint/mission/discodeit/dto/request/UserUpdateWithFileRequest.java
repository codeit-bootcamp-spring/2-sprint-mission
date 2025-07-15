package com.sprint.mission.discodeit.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record UserUpdateWithFileRequest(
        UserUpdateRequest userUpdateRequest,
        MultipartFile profileImage,
        Boolean removeProfileImage
) {
    public UserUpdateRequest getSafeUserUpdateRequest() {
        return userUpdateRequest != null ? userUpdateRequest : UserUpdateRequest.empty();
    }


    public static UserUpdateWithFileRequest of(UserUpdateRequest request, MultipartFile profileImage) {
        return new UserUpdateWithFileRequest(request, profileImage, false);
    }

    public static UserUpdateWithFileRequest of(UserUpdateRequest request, MultipartFile profileImage, Boolean removeProfileImage) {
        return new UserUpdateWithFileRequest(request, profileImage, removeProfileImage);
    }
}
