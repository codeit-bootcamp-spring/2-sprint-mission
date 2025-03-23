package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.application.dto.channel.ChannelRegisterDto;
import com.sprint.mission.discodeit.application.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.application.dto.message.MessageDto;
import com.sprint.mission.discodeit.application.dto.user.UserDto;
import com.sprint.mission.discodeit.application.dto.user.UserRegisterDto;
import com.sprint.mission.discodeit.command.handler.*;
import com.sprint.mission.discodeit.config.Beans;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.controller.MessageController;
import com.sprint.mission.discodeit.controller.UserController;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.view.InputView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static com.sprint.mission.discodeit.command.Command.*;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.LOGIN_USER;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.OTHER_USER;
import static com.sprint.mission.discodeit.view.OutputView.printHello;
import static com.sprint.mission.discodeit.view.OutputView.printServer;

public class Application {
    private static final String SETUP_CHANNEL_NAME = "general";

    public static void main(String[] args) {
        Beans beans = new Beans();
        UserController userController = beans.findBean(UserController.class);
        ChannelController channelController = beans.findBean(ChannelController.class);
        MessageController messageController = beans.findBean(MessageController.class);

        printHello();
        UserDto loginUser = setupUser(userController);
        ChannelResponseDto currentChannel = setupChannel(channelController, loginUser);

        Map<String, Handler> userCommandHandlers = new HashMap<>();
        try (Scanner scanner = new Scanner(System.in)) {
            InputView inputView = new InputView(scanner);
            createHandlers(userCommandHandlers, channelController, messageController, inputView);

            run(channelController, messageController, userCommandHandlers, loginUser, currentChannel, inputView);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void run(ChannelController channelController, MessageController messageController,
                            Map<String, Handler> handlers, UserDto loginUser, ChannelResponseDto currentChannel,
                            InputView inputView) {
        while (true) {
            List<MessageDto> currentChannelMessages = messageController.findByChannelId(currentChannel.id());
            printServer(channelController.findAll(loginUser.id()), loginUser, currentChannelMessages, currentChannel);

            String userCommand = readCommand(inputView);
            if (userCommand.equals(EXIT)) {
                break;
            }

            currentChannel = executeActionAndGetResponse(handlers, userCommand, currentChannel, loginUser);
        }
    }

    private static String readCommand(InputView inputView) {
        String userCommand = inputView.readCommand();
        validateUserCommandRange(userCommand);

        return userCommand;
    }

    private static void validateUserCommandRange(String userCommand) {
        if (Integer.parseInt(userCommand) > Integer.parseInt(EXIT) || Integer.parseInt(userCommand) < 1) {
            System.out.println("[ERROR] " + userCommand + "는 잘못된 명령 번호");
        }
    }

    private static ChannelResponseDto executeActionAndGetResponse(Map<String, Handler> handlers, String userCommand,
                                                                  ChannelResponseDto currentChannel, UserDto loginUser) {
        Handler handler = handlers.get(userCommand);
        return handler.execute(currentChannel, loginUser);
    }

    private static void createHandlers(Map<String, Handler> handlers, ChannelController channelController,
                                       MessageController messageController,
                                       InputView inputView) {
        handlers.put(CHANNEL_CREATION, new ChannelCreationHandler(channelController, inputView));
        handlers.put(USER_JOIN, new UserJoinHandler(channelController, inputView));
        handlers.put(CHANNEL_NAME_UPDATE, new ChannelNameUpdateHandler(channelController, inputView));
        handlers.put(MESSAGE_SEND, new MessageSendHandler(channelController, inputView,
                messageController));
        handlers.put(CHANNEL_CHANGE, new ChannelChangeHandler(channelController, inputView));
    }

    private static ChannelResponseDto setupChannel(ChannelController channelController, UserDto loginUser) {
        ChannelRegisterDto channelRegisterDto = new ChannelRegisterDto(ChannelType.PUBLIC, SETUP_CHANNEL_NAME, loginUser);

        return channelController.create(channelRegisterDto);
    }

    private static UserDto setupUser(UserController userController) {
        UserDto loginUser = userController.register(
                new UserRegisterDto(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword()), null
        );
        userController.register(
                new UserRegisterDto(OTHER_USER.getName(), OTHER_USER.getEmail(), OTHER_USER.getPassword()), null
        );
        return loginUser;
    }
}