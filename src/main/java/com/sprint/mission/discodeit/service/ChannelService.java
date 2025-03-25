package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.DTO.channelService.ChannelCreateDTO;


import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(ChannelCreateDTO channelCreateDto);
    Channel createByPrivate(ChannelCreateDTO channelCreateDto);
    Channel find(UUID channelId);
    List<Channel> findAll();
    Channel update(UUID channelId, String newName,  ChannelType newType);
    void delete(UUID channelId);
}
