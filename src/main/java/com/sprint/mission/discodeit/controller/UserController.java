package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.Scanner;
import java.util.UUID;

public class UserController {

    private final UserRepository userRepository = new FileUserRepository();
    private final UserService userService = new BasicUserService(userRepository);

    public UUID login(UUID userUUID, String password) {
        return userService.login(userUUID, password);
    }

    public void register(String nickname, String password) {
        userService.save(nickname, password);
    }
}
