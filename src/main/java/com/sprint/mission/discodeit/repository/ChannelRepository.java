package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel);
    Optional<Channel> findById(UUID id);
    List<Channel> findAll();
    List<Channel> findByType(ChannelType type);
    List<Channel> findByUserIdAndType(UUID userId, ChannelType type);
    void deleteById(UUID id);

//    void addMembers(UUID id, Set<UUID> userMembers, UUID userId);
//    void removeMembers(UUID id, Set<UUID> userMembers, UUID userId);

}
