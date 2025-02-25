package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(Channel channel);
    List<Channel> findAllChannel();
    Channel findByChannelId(UUID channelId);
    Channel updateChannel(UUID channelId, Channel channel);
    void deleteChannel(UUID channelId);

    void addUserToChannel(UUID channelId, UUID userID);
    void removeUserFromChannel(UUID channelId, UUID userID);
    List<Channel> findByUserId(UUID userID);
}
