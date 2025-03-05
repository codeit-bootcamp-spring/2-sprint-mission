package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    void create(String name);
    Channel findByChannelId(UUID channelId);
    List<Channel> findAll();
    void modify(UUID channelId, String name);
    void remove(UUID channelId);

    void addUser(Channel channel, User user);
    void removeUser(Channel channel, User user);
    void addMessage(Channel channel, Message message);
}
