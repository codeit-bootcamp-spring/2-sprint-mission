package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.application.dto.channel.ChannelRegisterDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelDto createPublic(ChannelRegisterDto channelRegisterDto);

    ChannelDto createPrivate(ChannelRegisterDto channelRegisterDto, List<UUID> channelMemberIds);

    ChannelDto addPrivateChannelMember(UUID channelId, UUID friendId);

    ChannelDto findById(UUID id);

    List<ChannelDto> findAllByUserId(UUID userId);

    ChannelDto updatePublicChannelName(UUID id, String name);

    void delete(UUID channelId);
}
