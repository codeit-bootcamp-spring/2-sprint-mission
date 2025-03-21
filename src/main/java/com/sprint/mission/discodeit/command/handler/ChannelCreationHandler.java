package com.sprint.mission.discodeit.command.handler;

import com.sprint.mission.discodeit.application.channel.ChannelDto;
import com.sprint.mission.discodeit.application.channel.ChannelRegisterDto;
import com.sprint.mission.discodeit.application.user.UserDto;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.view.InputView;

public class ChannelCreationHandler extends Handler {
    public ChannelCreationHandler(ChannelController channelController,
                                  InputView inputView) {
        super(channelController, inputView);
    }

    @Override
    public ChannelDto execute(ChannelDto currentChannel, UserDto loginUser) {
        String newChannelName = inputView.readCreationChannelName();
        ChannelRegisterDto channelRegisterDto = new ChannelRegisterDto(ChannelType.PRIVATE, newChannelName, loginUser);
        return channelController.create(channelRegisterDto);
    }
}
