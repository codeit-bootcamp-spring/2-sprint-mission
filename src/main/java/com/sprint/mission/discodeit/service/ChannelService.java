package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface ChannelService {
    Channel create(Channel channel);
    void addUserToChannel(String channelName, User user);
    List<Channel> findAll();
    Channel findByChannelName(String channelName);
    List<Channel> find(String identifier);
    Channel update(String channelName, Channel channel);
    void delete(String channelName);
    List<Message> getMessagesByChannel(Channel channel);
}
