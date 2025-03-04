package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileUserService;

import java.util.Optional;

public class JavaApplication_FileIO {
    public static void main(String[] args) {
        UserService userService = new FileUserService(); // File 기반 저장소 사용

        User user = new User("Jaeseok");
        userService.create(user);

        Optional<User> check = userService.read(user.getId());
        System.out.println("Loaded User: " + check.map(User::getUserName).orElse("User not found"));
    }
}
