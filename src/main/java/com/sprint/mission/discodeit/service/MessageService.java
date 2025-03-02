package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(String message, User user, Channel channel);
    void delete(UUID messageId);
    void update(UUID messageId, String newMessage);
    List<Message> readUser(String userName);
    List<Message> readChannel(String channelName);
    List<Message> readFullFilter(String userName, String channelName);
    List<Message> readAll();
}
