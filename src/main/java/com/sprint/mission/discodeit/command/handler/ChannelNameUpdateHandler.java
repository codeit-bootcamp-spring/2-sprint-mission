package com.sprint.mission.discodeit.command.handler;

import com.sprint.mission.discodeit.application.channeldto.ChannelResponseDto;
import com.sprint.mission.discodeit.application.userdto.UserDto;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.view.InputView;

public class ChannelNameUpdateHandler extends Handler {
    public ChannelNameUpdateHandler(ChannelController channelController,
                                    InputView inputView) {
        super(channelController, inputView);
    }

    @Override
    public ChannelResponseDto execute(ChannelResponseDto currentChannel, UserDto loginUser) {
        String channelName = inputView.readChangeChannelName();
        return channelController.updateNameToPublic(currentChannel.id(), channelName);
    }
}
