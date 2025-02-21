package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;


public interface ChannelService {
    void createChannel(Channel channel);
    Channel getChannel(String channelName);
    List<Channel> getAllChannels();
    void updateUser(UUID id, String channelName, String description);
    void deleteUser(UUID id);
}
