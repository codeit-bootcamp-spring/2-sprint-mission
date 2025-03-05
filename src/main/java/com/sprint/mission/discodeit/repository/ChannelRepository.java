package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.channel.Channel;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel);
    Channel find(UUID channelId);
    Set<UUID> findMemberIds(UUID channelId);
    List<Channel> findAll();
    void delete(UUID channelId);
    void addMember(UUID channelId, UUID userId);
    void removeMember(UUID channelId, UUID userId);
    boolean exists(UUID channelId);
}
