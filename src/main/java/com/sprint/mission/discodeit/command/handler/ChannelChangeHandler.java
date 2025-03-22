package com.sprint.mission.discodeit.command.handler;

import com.sprint.mission.discodeit.application.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.application.user.UserDto;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.view.InputView;

import java.util.List;

import static com.sprint.mission.discodeit.view.OutputView.printOtherChannels;

public class ChannelChangeHandler extends Handler {
    public ChannelChangeHandler(ChannelController channelController, InputView inputView) {
        super(channelController, inputView);
    }

    @Override
    public ChannelResponseDto execute(ChannelResponseDto currentChannel, UserDto loginUser) {
        List<ChannelResponseDto> channels = super.channelController.findAll(loginUser.id())
                .stream()
                .filter(channel -> !channel.id().equals(currentChannel.id()))
                .toList();

        printOtherChannels(channels);

        return channels.get(inputView.readChangeNumber());
    }
}
