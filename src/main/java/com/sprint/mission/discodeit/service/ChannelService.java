package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.UUID;
import java.util.Optional;

public interface ChannelService {
    Channel saveChannel(String name);
    void findAll();
    void findByName(String name);
    Optional<Channel> findById(UUID id);
    void update(UUID id, String name);
    void delete(UUID id);
}
