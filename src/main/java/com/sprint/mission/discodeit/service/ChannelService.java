package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayDeque;
import java.util.UUID;

public interface ChannelService {
    void createChannel(Channel newChannel);
    Channel readChannel(UUID channelId);
    ArrayDeque<Channel> readAllChannels();
    void updateChannelName(UUID channelId, String newChannelName);
    void addChannelParticipant(UUID channelId, User newParticipant);
    void deleteChannel(UUID channelId);
}
