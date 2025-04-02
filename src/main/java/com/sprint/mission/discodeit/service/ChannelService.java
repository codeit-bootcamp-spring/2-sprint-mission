package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.response.ChannelFindAllUserIdDto;
import com.sprint.mission.discodeit.dto.channel.request.ChannelPublicCreateDto;
import com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService{
    void create(ChannelPublicCreateDto channelPublicCreateDto);
    ChannelFindAllUserIdDto findById(UUID id);
    List<ChannelFindAllUserIdDto> findAllByUserId(UUID userId);
    void update(ChannelUpdateDto channelUpdateDto);
    void delete(UUID id);
}
