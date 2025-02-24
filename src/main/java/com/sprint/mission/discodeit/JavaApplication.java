package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;

public class JavaApplication {
    public static void main(String[] args) {
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService();

        // 등록
        User user1 = new User("codeit", "코드잇", "codeit@codeit.com", "123123");
        User user2 = new User("LYN", "이유나", "LYN@codeit.com", "456456");
        userService.create(user1);
        userService.create(user2);

        // 모든 데이터 조회
        System.out.println("* 등록된 사용자 리스트 *");
        List<User> users = userService.findAll();
        for (User u : users) {
            System.out.println("User ID: " + u.getUserId() + ", Name: " + u.getUserName() + ", Email: " + u.getUserEmail());
        }

        // 특정 데이터 조회
        User userFound  = userService.find("codeit");
        System.out.println("* 검색 : " + userFound.getUserId() + " *");
        if (userFound != null) {
            System.out.println("User ID: " + userFound.getUserId() + ", Name: " + userFound.getUserName() + ", Email: " + userFound.getUserEmail());
        } else {
            System.out.println("해당 ID는 등록되어 있지 않습니다.");
        }

        // 수정
        System.out.println("* 수정된 사용자 리스트 *");
        user2.setUserEmail("yuna@codeit.com");
        try {
            userService.update(user2.getUserId(), user2);
        } catch (IllegalArgumentException e) {
            System.out.println("업데이트 중 오류 발생: " + e.getMessage());
        }
        List<User> updateUsers = userService.findAll();
        for (User u : updateUsers) {
            System.out.println("User ID: " + u.getUserId() + ", Name: " + u.getUserName() + ", Email: " + u.getUserEmail());
        }

        // 삭제
        System.out.println("* 삭제된 후 남은 사용자 리스트 *");
        userService.delete("codeit");
        List<User> deleteUsers = userService.findAll();
        for (User u : deleteUsers) {
            System.out.println("User ID: " + u.getUserId() + ", Name: " + u.getUserName() + ", Email: " + u.getUserEmail());
        }

    }
}
