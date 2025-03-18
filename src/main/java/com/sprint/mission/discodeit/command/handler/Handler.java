package com.sprint.mission.discodeit.command.handler;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.view.InputView;

// TODO: 3/18/25 인터페이스로 변경예정
public abstract class Handler {
    protected final ChannelController channelController;
    protected final InputView inputView;

    protected Handler(ChannelController channelController, InputView inputView) {
        this.channelController = channelController;
        this.inputView = inputView;
    }

    public abstract ChannelDto execute(ChannelDto currentChannel, UserDto loginUser);
}
