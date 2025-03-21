package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChannelRepository {
    Channel save(Channel channel);
    List<Channel> findAllByUserId(UUID userId);
    Optional<Channel> findById(UUID channelId);
    boolean existsById(UUID channelId);
    void deleteById(UUID channelId);
}