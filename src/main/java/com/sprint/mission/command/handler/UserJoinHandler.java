package com.sprint.mission.command.handler;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.view.InputView;

public class UserJoinHandler extends Handler {
    public UserJoinHandler(ChannelController channelController, InputView inputView) {
        super(channelController, inputView);
    }

    @Override
    public ChannelDto execute(ChannelDto currentChannel, UserDto loginUser) {
        String friendEmail = inputView.readEmail();
        return channelController.addMember(currentChannel, friendEmail);
    }
}
