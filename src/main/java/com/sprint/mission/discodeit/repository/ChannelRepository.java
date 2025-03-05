package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    void create(Channel channel);
    void update(Channel channel);
    void delete(UUID id);

    List<Channel> findAll();
    Channel find(UUID id);
}
