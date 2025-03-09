package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    void save(Channel channel);
    Channel findById(UUID id);
    void delete(UUID id);
    void update(Channel channel);

    List<Channel> findAll();

    boolean existsById(UUID id);
}
