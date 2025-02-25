package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(UUID ownerId, String title, String description);
    UUID getChannelOwnerId(UUID channelId);
    Channel getChannelByChannelId(UUID channelId);
    List<Channel> getChannelsByTitle(String title);
    List<Channel> getAllChannels();
    Set<UUID> getChannelMembers(UUID channelId);
    Channel updateChannel(UUID channelId, String title, String description);
    void deleteChannelByChannelId(UUID channelId);
    Channel addUserToChannel(UUID channelId, UUID userId);
    void deleteUserFromChannel(UUID channelId, UUID userId);
    void deleteUserFromEveryChannel(UUID userId);
    void validateChannelId(UUID channelId);
}
