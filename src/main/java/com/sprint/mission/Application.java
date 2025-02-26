package com.sprint.mission;

import static com.sprint.mission.discodeit.view.InputView.readUserChoice;
import static com.sprint.mission.discodeit.view.OutputView.printServer;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.controller.UserController;

public class Application {
    public static void main(String[] args) {
        UserController userController = new UserController();
        ChannelController channelController = new ChannelController();

        System.out.println("안녕하세요 코드잇2기 서버입니다.");
        UserDto owner = userController.register(
                new UserRegisterDto("황지환", "hwang@naver.com", "12345")
        );
        userController.register(
                new UserRegisterDto("박지환", "park@naver.com", "12345")
        );

        ChannelDto initChannel = channelController.create("general", owner);
        printServer(initChannel, owner);
        readUserChoice();
    }
}