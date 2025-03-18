package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public UserDto register(UserRegisterDto userRegisterDto) {
        return userService.register(userRegisterDto);
    }

    public List<UserDto> findAll() {
        return userService.findAll();
    }
}
