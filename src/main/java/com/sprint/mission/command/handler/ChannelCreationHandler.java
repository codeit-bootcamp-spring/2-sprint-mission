package com.sprint.mission.command.handler;

import com.sprint.mission.command.handler.Handler;
import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.view.InputView;

public class ChannelCreationHandler extends Handler {
    public ChannelCreationHandler(ChannelController channelController,
                                  InputView inputView) {
        super(channelController, inputView);
    }

    @Override
    public ChannelDto execute(ChannelDto currentChannel, UserDto loginUser) {
        String newChannelName = inputView.readCreationChannelName();

        return channelController.create(newChannelName, loginUser);
    }
}
