package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(ChannelType type, String name, String description);
    Channel findById(UUID channelId);
    List<Channel> findAll();
    Channel updateName(UUID channelId, String newName);
    Channel updateDesc(UUID channelId, String newDescription);
    void delete(UUID channelId);
}
