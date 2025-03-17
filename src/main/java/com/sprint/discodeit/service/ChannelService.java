package com.sprint.discodeit.service;



import com.sprint.discodeit.domain.entity.Channel;
import com.sprint.discodeit.domain.entity.ChannelType;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(ChannelType type, String name, String description);
    Channel find(UUID channelId);
    List<Channel> findAll();
    Channel update(UUID channelId, String newName, String newDescription);
    void delete(UUID channelId);
}
