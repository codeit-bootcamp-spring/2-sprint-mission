package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ChannelRepository {
    void createChannel(Channel channel);
    void addMembers(UUID id, Set<UUID> userMembers, UUID userId);
    void removeMembers(UUID id, Set<UUID> userMembers, UUID userId);
    Optional<Channel> selectChannelById(UUID id);
    List<Channel> selectAllChannels();
    void updateChannel(UUID id, String name, String category, ChannelType type, UUID userId);
    void deleteChannel(UUID id, UUID userId);
}
