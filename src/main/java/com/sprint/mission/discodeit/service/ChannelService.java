package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.channelService.ChannelCreateByPrivateRequest;
import com.sprint.mission.discodeit.dto.channelService.ChannelDto;
import com.sprint.mission.discodeit.dto.channelService.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.dto.channelService.ChannelCreateRequest;


import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(ChannelCreateRequest channelCreateRequest);
    Channel createByPrivate(ChannelCreateByPrivateRequest channelCreateByPrivateRequest);
    Channel find(UUID channelId);
    ChannelDto findByStatus(UUID channelId);
    List<Channel> findAll();
    List<ChannelDto> findAllByUserId(UUID userId);
    Channel update(UUID channelId, ChannelUpdateRequest request);
    void delete(UUID channelId);
}
