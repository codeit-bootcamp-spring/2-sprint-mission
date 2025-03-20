package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;
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
}
