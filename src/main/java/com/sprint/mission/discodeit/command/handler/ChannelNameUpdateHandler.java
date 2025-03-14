package com.sprint.mission.discodeit.command.handler;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.view.InputView;

public class ChannelNameUpdateHandler extends Handler {
    public ChannelNameUpdateHandler(ChannelController channelController,
                                    InputView inputView) {
        super(channelController, inputView);
    }

    @Override
    public ChannelDto execute(ChannelDto currentChannel, UserDto loginUser) {
        String channelName = inputView.readChangeChannelName();
        return channelController.updateName(currentChannel, channelName);
    }
}
