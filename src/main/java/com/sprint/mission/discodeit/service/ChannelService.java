package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(UUID ownerId, String title, String description);
    User getChannelOwner(UUID channelId);
    Channel getChannelByChannelId(UUID channelId);
    List<Channel> getChannelsByTitle(String title);
    List<Channel> getChannelsByUserId(UUID userId);
    List<Channel> getAllChannels();
    Channel updateChannel(UUID channelId, UUID ownerId, String title, String description);
    boolean deleteChannelById(UUID channelId);
    Channel addUserToChannel(UUID channelId, UUID userId);
    Channel deleteUserFromChannel(UUID channelId, UUID userId);
}
