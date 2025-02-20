package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.UUID;

public interface ChannelService {
    void saveChannel(Channel channel);
    void findAll();
    void findByName(String name);
    void update(UUID id, String name);
    void delete(UUID id);
}
