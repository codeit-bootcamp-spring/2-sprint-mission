package com.sprint.sprint1.mission.view.output;

import com.sprint.sprint1.mission.model.entity.User;

public class MessageOutput {

    public void checkOutput(User user) {
        System.out.println(user.getUsername() + "님, 채팅을 시작합니다. ('종료' 입력 시 종료)");
    }

    public void displayMessage(String username, String message) {
        System.out.println(username + ": " + message);
    }
}
