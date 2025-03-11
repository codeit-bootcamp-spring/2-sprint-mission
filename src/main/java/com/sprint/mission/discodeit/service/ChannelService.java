package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    Channel saveChannel(String name);
    List<Channel> findAll();
    List<Channel> findByName(String name);
    Optional<Channel> findById(UUID id);
    void update(UUID id, String name);
    void delete(UUID id);
}
