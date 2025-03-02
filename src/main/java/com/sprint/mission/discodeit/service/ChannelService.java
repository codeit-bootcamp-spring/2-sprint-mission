package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(String channelName);
    Channel getChannel(UUID id);
    List<Channel> getAllChannels();

    void updateChannel(UUID id, String newChannelName);
    void deleteChannel(UUID id);
}
