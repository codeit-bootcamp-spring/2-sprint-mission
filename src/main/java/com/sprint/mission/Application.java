package com.sprint.mission;

import static com.sprint.mission.discodeit.config.SetUpUserInfo.LONGIN_USER;
import static com.sprint.mission.discodeit.config.SetUpUserInfo.OTHER_USER;
import static com.sprint.mission.discodeit.view.InputView.readCommand;
import static com.sprint.mission.discodeit.view.OutputView.printHello;
import static com.sprint.mission.discodeit.view.OutputView.printServer;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.MessageDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;
import com.sprint.mission.discodeit.config.Beans;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.controller.MessageController;
import com.sprint.mission.discodeit.controller.UserController;
import com.sprint.mission.discodeit.view.ChannelCommand;
import java.util.List;

public class Application {
    private static final String SETUP_CHANNEL_NAME = "general";

    public static void main(String[] args) {
        printHello();
        Beans beans = new Beans();
        UserDto loginUser = registerSetupUser(beans.findBean(UserController.class));

        ChannelDto currentChannel = (beans.findBean(ChannelController.class)).create(SETUP_CHANNEL_NAME, loginUser);
        while (true) {
            List<MessageDto> currentChannelMessages = (beans.findBean(MessageController.class)).findByChannelId(
                    currentChannel.id());
            printServer((beans.findBean(ChannelController.class)).findAll(), loginUser, currentChannelMessages,
                    currentChannel);

            try {
                currentChannel = ChannelCommand.fromNumber(readCommand())
                        .execute((beans.findBean(ChannelController.class)),
                                (beans.findBean(MessageController.class)), loginUser, currentChannel);
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
                new UserRegisterDto(LONGIN_USER.getName(), LONGIN_USER.getEmail(), LONGIN_USER.getPassword())
        );
        userController.register(
                new UserRegisterDto(OTHER_USER.getName(), OTHER_USER.getEmail(), OTHER_USER.getPassword())
        );
        return loginUser;
    }
}