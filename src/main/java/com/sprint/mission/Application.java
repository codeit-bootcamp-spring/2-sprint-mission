package com.sprint.mission;

import static com.sprint.mission.config.SetUpUserInfo.LONGIN_USER;
import static com.sprint.mission.config.SetUpUserInfo.OTHER_USER;
import static com.sprint.mission.discodeit.view.InputView.readCommand;
import static com.sprint.mission.discodeit.view.OutputView.printHello;
import static com.sprint.mission.discodeit.view.OutputView.printServer;

import com.sprint.mission.config.Beans;
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
    private static final String SETUP_CHANNEL_NAME = "general";

    public static void main(String[] args) {
        Beans beans = new Beans();
        UserController userController = beans.findBean(UserController.class);
        ChannelController channelController = beans.findBean(ChannelController.class);
        MessageController messageController = beans.findBean(MessageController.class);

        printHello();
        UserDto loginUser = setupUser(userController);
        ChannelDto currentChannel = setupChannel(channelController, loginUser);
        while (true) {
            List<MessageDto> currentChannelMessages = messageController.findByChannelId(currentChannel.id());
            printServer(channelController.findAll(), loginUser, currentChannelMessages, currentChannel);

            try {
                currentChannel = ChannelCommand.fromNumber(readCommand())
                        .execute(channelController, messageController, loginUser, currentChannel);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
            if (currentChannel == null) {
                break;
            }
        }
    }

    private static ChannelDto setupChannel(ChannelController channelController, UserDto loginUser) {
        return channelController.create(SETUP_CHANNEL_NAME, loginUser);
    }

    private static UserDto setupUser(UserController userController) {
        UserDto loginUser = userController.register(
                new UserRegisterDto(LONGIN_USER.getName(), LONGIN_USER.getEmail(), LONGIN_USER.getPassword())
        );
        userController.register(
                new UserRegisterDto(OTHER_USER.getName(), OTHER_USER.getEmail(), OTHER_USER.getPassword())
        );
        return loginUser;
    }
}