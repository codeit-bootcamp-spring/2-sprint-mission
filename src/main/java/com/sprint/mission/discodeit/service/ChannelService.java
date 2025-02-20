package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
        Channel createChannel(Channel channel);
        Channel readChannel(UUID id);
        List<Channel> readAllChannels();
        Channel updateChannel(UUID id, Channel channel);
        void deleteChannel(UUID id);
}
