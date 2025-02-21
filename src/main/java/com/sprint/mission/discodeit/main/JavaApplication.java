package com.sprint.mission.discodeit.main;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class JavaApplication {
    public static void main(String[] args) {
        UserService userService = new JCFUserService();

        User user1 = userService.createUser("Kim");
        User user2 = userService.createUser("Lee");

        System.out.println("=== 유저 등록 완료 ===");
        System.out.println("User 1: " + user1.getId() + " | Name: " + user1.getUsername());
        System.out.println("User 2: " + user2.getId() + " | Name: " + user2.getUsername());

        User fetchedUser = userService.getUser(user1.getId());
        System.out.println("=== 유저 조회 ===");
        System.out.println("Fetched User: " + fetchedUser.getId() + " | Name: " + fetchedUser.getUsername());

        System.out.println("=== 모든 유저 조회 ===");
        for (User u : userService.getAllUsers()) {
            System.out.println("ID: " + u.getId() + " | Name: " + u.getUsername());
        }

        userService.updateUser(user1.getId(), "Park");
        System.out.println("=== 유저 정보 수정 완료 ===");
        System.out.println("Updated User 1: " + userService.getUser(user1.getId()).getUsername());

        userService.deleteUser(user2.getId());
        System.out.println("=== 유저 삭제 완료 ===");
        System.out.println("User 2 Exists? " + (userService.getUser(user2.getId()) != null));

        System.out.println("=== 삭제 후 모든 유저 조회 ===");
        for (User u : userService.getAllUsers()) {
            System.out.println("ID: " + u.getId() + " | Name: " + u.getUsername());
        }
    }
}
