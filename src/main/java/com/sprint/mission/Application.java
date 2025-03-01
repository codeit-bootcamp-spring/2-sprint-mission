package com.sprint.mission;

import static com.sprint.mission.discodeit.view.InputView.readUserChoice;
import static com.sprint.mission.discodeit.view.OutputView.printHello;
import static com.sprint.mission.discodeit.view.OutputView.printServer;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.MessageDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.controller.MessageController;
import com.sprint.mission.discodeit.controller.UserController;
import com.sprint.mission.discodeit.view.ChannelCommand;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        UserController userController = new UserController();
        ChannelController channelController = new ChannelController();
        MessageController messageController = new MessageController();

        UserDto loginUser = registerSetupUser(userController);

        printHello();
        ChannelDto currentChannel = channelController.create("general", loginUser);
        while (true) {
            List<MessageDto> currentChannelMessages = messageController.findByChannelId(currentChannel.id());
            printServer(channelController.findAll(), loginUser, currentChannelMessages, currentChannel);

            try {
                currentChannel = ChannelCommand.fromNumber(readUserChoice())
                        .execute(channelController, messageController, loginUser, currentChannel);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
            if (currentChannel == null) {
                break;
            }
        }
    }

    private static UserDto registerSetupUser(UserController userController) {
        UserDto loginUser = userController.register(
                new UserRegisterDto("황지환", "hwang@naver.com", "12345")
        );
        userController.register(
                new UserRegisterDto("박지환", "park@naver.com", "12345")
        );
        return loginUser;
    }
}