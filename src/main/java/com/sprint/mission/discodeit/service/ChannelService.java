package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.lang.reflect.Member;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    void create(Channel channel);
    Channel find(UUID id);
    List<Channel> findAll();
    void update(Channel channel);
    void delete(UUID id);

    void addMember(UUID channelId, User user);
    void removeMember(UUID channelId, User user);

    List<User> findMembers(UUID channelId);
}
