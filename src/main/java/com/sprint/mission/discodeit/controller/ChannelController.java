package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.ChannelRegisterDto;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    public ChannelDto create(ChannelRegisterDto channelRegisterDto) {
        return channelService.create(channelRegisterDto);
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
