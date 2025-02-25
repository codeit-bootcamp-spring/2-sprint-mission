package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.Channel;

import java.util.Map;
import java.util.UUID;

public interface ChannelService {
    void create(String channelName);
    void delete(String channelName);
    void update(String oldName, String newName);
    Channel read(String channelName);
    Map<UUID, Channel> readAll();
}
