package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel getChannel(UUID cid);
    List<Channel> getAllChannels();
    void registerChannel(String channelName, String userName);
    void updateChannel(UUID cid, String channelName);
    void deleteChannel(UUID cid);
}
