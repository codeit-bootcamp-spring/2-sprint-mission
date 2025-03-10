package com.sprint.mission.discodeit.convertfile.fileservice;

import com.sprint.mission.discodeit.convertfile.entity.ChannelType;
import com.sprint.mission.discodeit.convertfile.entity.Channel;
import com.sprint.mission.discodeit.convertfile.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(ChannelType type, String name, String description);
    Channel find(UUID channelId);
    List<Channel> findAll();
    Channel update(UUID channelId, String newName, String newDescription);
    void delete(UUID channelId);
}
