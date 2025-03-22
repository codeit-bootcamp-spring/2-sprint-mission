package com.sprint.mission.discodeit.command.handler;

import com.sprint.mission.discodeit.application.channeldto.ChannelRegisterDto;
import com.sprint.mission.discodeit.application.channeldto.ChannelResponseDto;
import com.sprint.mission.discodeit.application.userdto.UserDto;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.view.InputView;

public class ChannelCreationHandler extends Handler {
    public ChannelCreationHandler(ChannelController channelController,
                                  InputView inputView) {
        super(channelController, inputView);
    }

    @Override
    public ChannelResponseDto execute(ChannelResponseDto currentChannel, UserDto loginUser) {
        String newChannelName = inputView.readCreationChannelName();
        ChannelRegisterDto channelRegisterDto = new ChannelRegisterDto(ChannelType.PRIVATE, newChannelName, loginUser);
        return channelController.create(channelRegisterDto);
    }
}
