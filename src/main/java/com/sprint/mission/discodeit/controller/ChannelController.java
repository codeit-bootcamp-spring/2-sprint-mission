package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import java.util.*;

public class ChannelController {
    private final ChannelService channelService = JCFChannelService.getInstance();

    public ChannelDto create(String name, UserDto owner) {
        return channelService.create(name, owner);
    }

    public List<ChannelDto> findAll() {
        return channelService.findAll();
    }

    public ChannelDto updateName(ChannelDto channelDto, String channelName) {
        channelService.updateName(channelDto.id(), channelName);

        return channelService.findById(channelDto.id());
    }

    public ChannelDto addMember(ChannelDto channelDto, String friendEmail) {
        return channelService.addMember(channelDto.id(), friendEmail);
    }
}
