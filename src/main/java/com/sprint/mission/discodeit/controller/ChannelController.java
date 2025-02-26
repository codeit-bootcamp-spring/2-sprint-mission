package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;

public class ChannelController {
    private final ChannelService channelService = new JCFChannelService();

    public ChannelDto create(String name, UserDto owner){
        return channelService.create(name, owner);
    }
}
