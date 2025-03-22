package com.sprint.mission.discodeit.command.handler;

import com.sprint.mission.discodeit.application.channeldto.ChannelResponseDto;
import com.sprint.mission.discodeit.application.messagedto.MessageCreationDto;
import com.sprint.mission.discodeit.application.userdto.UserDto;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.controller.MessageController;
import com.sprint.mission.discodeit.view.InputView;

import java.util.ArrayList;

public class MessageSendHandler extends Handler {
    private final MessageController messageController;

    public MessageSendHandler(ChannelController channelController, InputView inputView,
                              MessageController messageController) {
        super(channelController, inputView);
        this.messageController = messageController;
    }

    @Override
    public ChannelResponseDto execute(ChannelResponseDto currentChannel, UserDto loginUser) {
        String message = inputView.readMessage();
        messageController.createMessage(new MessageCreationDto(message, currentChannel.id(), loginUser.id()), new ArrayList<>());

        return currentChannel;
    }
}
