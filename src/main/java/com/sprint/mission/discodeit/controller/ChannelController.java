package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.channel.ChannelDto;
import com.sprint.mission.discodeit.application.channel.ChannelRegisterDto;
import com.sprint.mission.discodeit.application.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;
    private final MessageService messageService;
    private final UserService userService;

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
        return channelService.addMemberToPrivate(channelDto.id(), friendEmail);
    }

    public ChannelResponseDto findById(UUID channelId) {
        Instant lastMessageCreatedAt = messageService.findLastMessageCreatedAtByChannelId(channelId);
        ChannelDto channel = channelService.findById(channelId);

        if (channel.type().equals(ChannelType.PRIVATE)) {
            ChannelResponseDto.fromEntity(channel, lastMessageCreatedAt, channel.usersDto());
        }

        return ChannelResponseDto.fromEntity(channel, lastMessageCreatedAt, null);
    }
}
