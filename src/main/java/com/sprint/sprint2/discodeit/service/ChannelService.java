package com.sprint.sprint2.discodeit.service;



import com.sprint.sprint2.discodeit.entity.Channel;
import com.sprint.sprint2.discodeit.entity.ChannelType;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(ChannelType type, String name, String description);
    Channel find(UUID channelId);
    List<Channel> findAll();
    Channel update(UUID channelId, String newName, String newDescription);
    void delete(UUID channelId);
}
