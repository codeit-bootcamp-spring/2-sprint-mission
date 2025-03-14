package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    public UUID login(UUID userUUID, String password) {
        return userService.login(userUUID, password);
    }

    public void register(String nickname, String password) {
        userService.save(nickname, password);
    }

    public User findByUser(UUID userUUID) {
        return userService.findByUser(userUUID);
    }

    public List<User> findAll() {
        return userService.findAllUser();
    }

    public void updateNickname(UUID userUUID, String nickname) {
        userService.update(userUUID, nickname);
    }

    public void deleteUser(UUID userUUID) {
        userService.delete(userUUID);
    }

}
