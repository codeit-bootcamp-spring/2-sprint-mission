package com.sprint.mission.discodeit.command.handler;

import com.sprint.mission.discodeit.application.channel.ChannelDto;
import com.sprint.mission.discodeit.application.user.UserDto;
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
