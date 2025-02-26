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

    // TODO: 2/26/25 계속 채널 Dto를 만드는게 좋은지 고민해봐야됨, 그리고 ChannelDto에 id말고 다른거 반환하는게 좋은지 생각해봐야된다
    public ChannelDto updateName(ChannelDto channelDto, String channelName) {
        channelService.updateName(channelDto.id(), channelName);

        return channelService.findById(channelDto.id());
    }

    public ChannelDto addMember(ChannelDto channelDto, String friendEmail) {
        return channelService.addMember(channelDto.id(), friendEmail);
    }
}
