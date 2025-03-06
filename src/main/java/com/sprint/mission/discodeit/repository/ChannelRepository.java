package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    boolean channelExists(UUID channelId);

    Channel findById(UUID channelId);

    List<Channel> findAll();

    List<Channel> findUpdatedChannels();

    void createChannel(String channelName, User user);

    void updateChannel(UUID channelId, String channelName);

    void deleteChannel(UUID channelId);

    List<UUID> channelListByuserId(UUID userId);

    void deleteChannelList(List<UUID> channelIdList);
}
