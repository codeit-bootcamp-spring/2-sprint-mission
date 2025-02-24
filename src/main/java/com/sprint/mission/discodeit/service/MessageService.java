package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;

public interface MessageService {
    Message create(Message message);
    List<Message> findByUserId(String userId);
    List<Message> findByMessageContent(String messageContent);
    List<Message> findByChannel(Channel channel);
    List<Message> findAll();
    void delete(String messageContent);
}
