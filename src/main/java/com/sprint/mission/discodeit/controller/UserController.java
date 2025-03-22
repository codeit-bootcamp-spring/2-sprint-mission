package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.userdto.UserDto;
import com.sprint.mission.discodeit.application.userdto.UserRegisterDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
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

    public UserDto register(UserRegisterDto userRegisterDto, MultipartFile multipartFile) {
        UUID profileId = binaryContentService.createProfileImage(multipartFile);

        return userService.register(userRegisterDto, profileId);
    }

    public List<UserDto> findAll() {
        return userService.findAll();
    }

    public UserDto updateProfile(UUID userId, MultipartFile multipartFile) {
        UserDto beforeUpdatedUser = userService.findById(userId);
        UUID profileId = binaryContentService.createProfileImage(multipartFile);
        UserDto user = userService.updateProfileImage(userId, profileId);
        binaryContentService.delete(beforeUpdatedUser.profileId());

        return user;
    }

    public void delete(UUID userId) {
        UserDto beforeUpdatedUser = userService.findById(userId);
        userService.delete(userId);
        binaryContentService.delete(beforeUpdatedUser.profileId());
    }
}
