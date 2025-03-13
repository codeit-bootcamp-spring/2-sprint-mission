package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.controller.MessageController;
import java.util.List;

public enum ChannelCommand {
    CHANNEL_CREATION("1") {
        @Override
        public ChannelDto execute(ChannelController channelController, MessageController messageController,
                                  UserDto loginUser,
                                  ChannelDto currentChannel, InputView inputView) {

            return channelController.create(inputView.readNewChannelName(), loginUser);
        }
    },
    USER_ADDITION("2") {
        @Override
        public ChannelDto execute(ChannelController channelController, MessageController messageController,
                                  UserDto loginUser,
                                  ChannelDto currentChannel, InputView inputView) {

            return channelController.addMember(currentChannel, inputView.readEmail());
        }
    },
    NAME_CHANGE("3") {
        @Override
        public ChannelDto execute(ChannelController channelController, MessageController messageController,
                                  UserDto loginUser,
                                  ChannelDto currentChannel, InputView inputView) {

            return channelController.updateName(currentChannel, inputView.readChannelName());
        }
    },
    MESSAGE_SENT("4") {
        @Override
        public ChannelDto execute(ChannelController channelController, MessageController messageController,
                                  UserDto loginUser,
                                  ChannelDto currentChannel, InputView inputView) {
            messageController.createMessage(inputView.readMessage(), currentChannel.id(), loginUser.id());

            return currentChannel;
        }
    },
    CHANNEL_CHANGE("5") {
        @Override
        public ChannelDto execute(ChannelController channelController, MessageController messageController,
                                  UserDto loginUser, ChannelDto currentChannel, InputView inputView) {
            List<ChannelDto> channels = channelController.findAll()
                    .stream()
                    .filter(channel -> !channel.equals(currentChannel))
                    .toList();

            return channels.get(inputView.readChangeChannelNumber(channels) - 1);
        }
    },
    EXIT("6") {
        @Override
        public ChannelDto execute(ChannelController channelController, MessageController messageController,
                                  UserDto loginUser,
                                  ChannelDto currentChannel, InputView inputView) {

            return null;
        }
    };

    private final String number;

    public abstract ChannelDto execute(ChannelController channelController, MessageController messageController,
                                       UserDto loginUser, ChannelDto currentChannel, InputView inputView);

    ChannelCommand(String number) {
        this.number = number;
    }

    public static ChannelCommand fromNumber(String number) {
        for (ChannelCommand command : values()) {
            if (command.number.equals(number)) {
                return command;
            }
        }
        throw new IllegalArgumentException("[ERROR] " + number + "는 잘못된 명령 번호");
    }
}
