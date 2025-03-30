package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.user.UserRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResponse;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
import com.sprint.mission.discodeit.application.dto.userstatus.UserStatusResult;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final BinaryContentService binaryContentService;
    private final UserStatusService userStatusService;

    public UserResult register(UserRequest userRequest, MultipartFile multipartFile) {
        UUID profileId = binaryContentService.createProfileImage(multipartFile);

        return userService.register(userRequest, profileId);
    }

    public UserResponse findById(UUID userId) {
        UserResult userResult = userService.findById(userId);
        UserStatusResult userStatusDto = userStatusService.findByUserId(userResult.id());

        return UserResponse.of(userResult, userStatusDto.isLogin());
    }

    public List<UserResult> findAll() {
        return userService.findAll();
    }

    public UserResult updateProfile(UUID userId, MultipartFile multipartFile) {
        UserResult beforeUpdatedUser = userService.findById(userId);
        UUID profileId = binaryContentService.createProfileImage(multipartFile);
        UserResult user = userService.updateProfileImage(userId, profileId);
        binaryContentService.delete(beforeUpdatedUser.profileId());

        return user;
    }

    public void delete(UUID userId) {
        UserResult beforeUpdatedUser = userService.findById(userId);
        userService.delete(userId);
        binaryContentService.delete(beforeUpdatedUser.profileId());
    }
}
