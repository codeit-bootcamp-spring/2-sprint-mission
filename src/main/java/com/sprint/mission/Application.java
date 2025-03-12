package com.sprint.mission;

import static com.sprint.mission.command.Command.CHANNEL_CHANGE;
import static com.sprint.mission.command.Command.CHANNEL_CREATION;
import static com.sprint.mission.command.Command.CHANNEL_NAME_UPDATE;
import static com.sprint.mission.command.Command.EXIT;
import static com.sprint.mission.command.Command.MESSAGE_SEND;
import static com.sprint.mission.command.Command.USER_JOIN;
import static com.sprint.mission.config.SetUpUserInfo.LONGIN_USER;
import static com.sprint.mission.config.SetUpUserInfo.OTHER_USER;
import static com.sprint.mission.discodeit.view.OutputView.printHello;
import static com.sprint.mission.discodeit.view.OutputView.printServer;

import com.sprint.mission.command.handler.ChannelChangeHandler;
import com.sprint.mission.command.handler.ChannelCreationHandler;
import com.sprint.mission.command.handler.ChannelNameUpdateHandler;
import com.sprint.mission.command.handler.Handler;
import com.sprint.mission.command.handler.MessageSendHandler;
import com.sprint.mission.command.handler.UserJoinHandler;
import com.sprint.mission.config.Beans;
import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.MessageDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.controller.MessageController;
import com.sprint.mission.discodeit.controller.UserController;
import com.sprint.mission.discodeit.view.InputView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
                            Map<String, Handler> handlers, UserDto loginUser, ChannelDto currentChannel,
                            InputView inputView) {
        while (true) {
            List<MessageDto> currentChannelMessages = messageController.findByChannelId(currentChannel.id());
            printServer(channelController.findAll(), loginUser, currentChannelMessages, currentChannel);

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

    private static ChannelDto executeActionAndGetResponse(Map<String, Handler> handlers, String userCommand,
                                                          ChannelDto currentChannel, UserDto loginUser) {
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