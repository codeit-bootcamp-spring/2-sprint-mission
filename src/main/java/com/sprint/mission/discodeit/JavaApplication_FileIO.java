package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileUserService;

import java.util.Optional;

public class JavaApplication_FileIO {
    public static void main(String[] args) {
        try {
            UserService userService = new FileUserService();

            User user = new User("Jaeseok");
            userService.create(user);
            Optional<User> check = userService.read(user.getId());
            System.out.println("Created User: " + check.map(User::getUserName).orElse("User not found"));
        } catch (Exception e) {
            e.printStackTrace(); // 오류 메시지를 콘솔에 출력
        }
    }
}
