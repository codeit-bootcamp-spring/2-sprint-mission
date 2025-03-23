package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelPublicCreateDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService{
    void create(ChannelPublicCreateDto channelPublicCreateDto);
    ChannelDto findById(UUID id);
    List<ChannelDto> findAllByUserId(UUID userId);
    void update(ChannelUpdateDto channelUpdateDto);
    void delete(UUID id);
}
