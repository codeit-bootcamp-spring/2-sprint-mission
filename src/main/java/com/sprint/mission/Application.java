package com.sprint.mission;

import static com.sprint.mission.discodeit.view.InputView.readUserChoice;
import static com.sprint.mission.discodeit.view.OutputView.printHello;
import static com.sprint.mission.discodeit.view.OutputView.printServer;

import com.sprint.mission.discodeit.BeanFactory;
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
        printHello();
        BeanFactory beanFactory = new BeanFactory();
        UserDto loginUser = registerSetupUser(beanFactory.findBean(UserController.class));

        ChannelDto currentChannel = (beanFactory.findBean(ChannelController.class)).create("general", loginUser);
        while (true) {
            List<MessageDto> currentChannelMessages = (beanFactory.findBean(MessageController.class)).findByChannelId(currentChannel.id());
            printServer((beanFactory.findBean(ChannelController.class)).findAll(), loginUser, currentChannelMessages, currentChannel);

            try {
                currentChannel = ChannelCommand.fromNumber(readUserChoice())
                        .execute((beanFactory.findBean(ChannelController.class)), (beanFactory.findBean(MessageController.class)), loginUser, currentChannel);
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