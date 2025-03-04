package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileUserService;

import java.util.List;

public class User_READTEST {
    public static void main(String[] args) {
        UserService userService = new FileUserService();

        // 저장된 사용자 리스트 확인
        List<User> users = userService.readAll();
        System.out.println("Loaded Users: " + users.size());

        for (User user : users) {
            System.out.println("User: " + user.getUserName());
        }
    }
}
