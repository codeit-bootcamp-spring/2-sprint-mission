package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void create(String message, User user, Channel channel);
    void delete(String userName ,String message, Long timestamp);
    void update(String userName, Long timestamp , String oldMessage, String newMessage);
    List<Message> readUser(String userName);
    List<Message> readChannel(String channelName);
    List<Message> readFullFilter(String userName, String channelName);
    List<Message> readAll();
}
