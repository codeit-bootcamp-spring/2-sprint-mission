package com.sprint.discodeit.sprint4.service;



import com.sprint.discodeit.sprint5.domain.ChannelType;
import com.sprint.discodeit.sprint5.domain.entity.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(ChannelType type, String name, String description);
    Channel find(UUID channelId);
    List<Channel> findAll();
    Channel update(UUID channelId, String newName, String newDescription);
    void delete(UUID channelId);
}
