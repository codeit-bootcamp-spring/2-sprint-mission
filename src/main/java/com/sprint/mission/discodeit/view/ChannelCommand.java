package com.sprint.mission.discodeit.view;

import static com.sprint.mission.discodeit.view.InputView.readChangeChannelNumber;
import static com.sprint.mission.discodeit.view.InputView.readChannelName;
import static com.sprint.mission.discodeit.view.InputView.readEmail;
import static com.sprint.mission.discodeit.view.InputView.readMessage;
import static com.sprint.mission.discodeit.view.InputView.readNewChannelName;

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
                                  ChannelDto currentChannel) {

            return channelController.create(readNewChannelName(), loginUser);
        }
    },
    USER_ADDITION("2") {
        @Override
        public ChannelDto execute(ChannelController channelController, MessageController messageController,
                                  UserDto loginUser,
                                  ChannelDto currentChannel) {

            return channelController.addMember(currentChannel, readEmail());
        }
    },
    NAME_CHANGE("3") {
        @Override
        public ChannelDto execute(ChannelController channelController, MessageController messageController,
                                  UserDto loginUser,
                                  ChannelDto currentChannel) {

            return channelController.updateName(currentChannel, readChannelName());
        }
    },
    MESSAGE_SENT("4") {
        @Override
        public ChannelDto execute(ChannelController channelController, MessageController messageController,
                                  UserDto loginUser,
                                  ChannelDto currentChannel) {
            messageController.createMessage(readMessage(), currentChannel.id(), loginUser.id());

            return currentChannel;
        }
    },
    CHANNEL_CHANGE("5") {
        @Override
        public ChannelDto execute(ChannelController channelController, MessageController messageController,
                                  UserDto loginUser, ChannelDto currentChannel) {
            List<ChannelDto> channels = channelController.findAll()
                    .stream()
                    .filter(channel -> !channel.equals(currentChannel))
                    .toList();

            return channels.get(readChangeChannelNumber(channels) - 1);
        }
    },
    EXIT("6") {
        @Override
        public ChannelDto execute(ChannelController channelController, MessageController messageController,
                                  UserDto loginUser,
                                  ChannelDto currentChannel) {

            return null;
        }
    };

    private final String number;

    public abstract ChannelDto execute(ChannelController channelController, MessageController messageController,
                                       UserDto loginUser, ChannelDto currentChannel);

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
