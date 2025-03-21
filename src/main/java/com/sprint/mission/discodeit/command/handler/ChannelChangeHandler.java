package com.sprint.mission.discodeit.command.handler;

import com.sprint.mission.discodeit.application.channel.ChannelDto;
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
    public ChannelDto execute(ChannelDto currentChannel, UserDto loginUser) {
        List<ChannelDto> channels = super.channelController.findAll()
                .stream()
                .filter(channel -> !channel.equals(currentChannel))
                .toList();

        printOtherChannels(channels);

        return channels.get(inputView.readChangeNumber());
    }
}
