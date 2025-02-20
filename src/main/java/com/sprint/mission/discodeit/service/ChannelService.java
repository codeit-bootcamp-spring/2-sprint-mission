package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(User owner, String title, String description);
    Channel getChannelById(UUID channelId);
    List<Channel> getChannels();
    Channel updateChannel(UUID channelId, User owner, String title, String description);
    boolean deleteChannelById(UUID channelId);
}
