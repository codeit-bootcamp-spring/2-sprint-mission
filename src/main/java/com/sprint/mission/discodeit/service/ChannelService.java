package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;



public interface ChannelService {
    void createChannel(Channel channel);
    void getChannel(String channelName);
    void getAllChannels();
    void updateChannel(String channelName, String changeChannel, String changeDescription);
    void deleteChannel(String channelName);
}
