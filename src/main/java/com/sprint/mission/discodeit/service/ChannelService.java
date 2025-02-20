package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface ChannelService {
    Channel createChannel(User owner, String title, String description);
    Channel getChannelById(Long channelId);
    List<Channel> getChannels();
    Channel updateChannel(Long channelId, User owner, String title, String description);
    boolean deleteChannelById(Long channelId);
}
