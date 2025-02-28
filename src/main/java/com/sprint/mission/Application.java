package com.sprint.mission;

import static com.sprint.mission.discodeit.view.InputView.readChannelName;
import static com.sprint.mission.discodeit.view.InputView.readEmail;
import static com.sprint.mission.discodeit.view.InputView.readMessage;
import static com.sprint.mission.discodeit.view.InputView.readNewChannelName;
import static com.sprint.mission.discodeit.view.InputView.readUserChoice;
import static com.sprint.mission.discodeit.view.OutputView.printServer;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.MessageDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.controller.MessageController;
import com.sprint.mission.discodeit.controller.UserController;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        UserController userController = new UserController();
        ChannelController channelController = new ChannelController();
        MessageController messageController = new MessageController();

        System.out.println("안녕하세요 코드잇2기 서버입니다.");
        UserDto loginUser = userController.register(
                new UserRegisterDto("황지환", "hwang@naver.com", "12345")
        );
        userController.register(
                new UserRegisterDto("박지환", "park@naver.com", "12345")
        );
        ChannelDto initChannel = channelController.create("general", loginUser);
        List<MessageDto> initMessages = messageController.findByChannelId(initChannel.id());
        printServer(channelController.findAll(), loginUser, initMessages, initChannel);

        while (true) {
            String userChoice = readUserChoice();
            if (userChoice.equals("1")) {
                ChannelDto currentChannel = channelController.create(readNewChannelName(), loginUser);
                List<MessageDto> currentChannelMessages = messageController.findByChannelId(currentChannel.id());
                printServer(channelController.findAll(), loginUser, currentChannelMessages, currentChannel);
            }
            if (userChoice.equals("5")) {
                messageController.createMessage(readMessage(), initChannel.id(), loginUser.id());
                List<MessageDto> currentChannelMessages = messageController.findByChannelId(initChannel.id());
                printServer(channelController.findAll(), loginUser, currentChannelMessages, initChannel);
            }
            if (userChoice.equals("4")) {
                ChannelDto currentChannel = channelController.addMember(initChannel, readEmail());
                List<MessageDto> currentChannelMessages = messageController.findByChannelId(currentChannel.id());
                printServer(channelController.findAll(), loginUser, currentChannelMessages, currentChannel);
            }
            if (userChoice.equals("3")) {
                ChannelDto currentChannel = channelController.updateName(initChannel, readChannelName());
                List<MessageDto> currentChannelMessages = messageController.findByChannelId(initChannel.id());
                printServer(channelController.findAll(), loginUser, currentChannelMessages, currentChannel);
            }
            if (userChoice.equals("7")) {
                break;
            }
        }
    }
}