package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ChannelRepository {
    UUID createChannel(Channel channel);
    void addMembers(UUID id, Set<UUID> userMembers, UUID userId);
    void removeMembers(UUID id, Set<UUID> userMembers, UUID userId);
    Channel findById(UUID id);
    List<Channel> findAll();
    List<Channel> findByType(ChannelType type);
    List<Channel> findByUserIdAndType(UUID userId, ChannelType type);
    void updateChannel(UUID id, String name, String category, ChannelType type, UUID userId);
    void deleteChannel(UUID id, UUID userId);
}
