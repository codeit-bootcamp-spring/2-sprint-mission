package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class UserController {
    private final UserService userService = JCFUserService.getInstance();

    public UserDto register(UserRegisterDto userRegisterDto){
        return userService.register(userRegisterDto);
    }
}
