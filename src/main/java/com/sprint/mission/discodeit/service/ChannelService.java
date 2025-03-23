package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.application.dto.channel.ChannelRegisterDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelDto addMemberToPrivate(UUID channelId, UUID friendId);

    ChannelDto create(ChannelRegisterDto channelRegisterDto);

    ChannelDto findById(UUID id);

    List<ChannelDto> findAllByUserId(UUID userId);

    ChannelDto updateName(UUID id, String name);

    void delete(UUID channelId);
}
