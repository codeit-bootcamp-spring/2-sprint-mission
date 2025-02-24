package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;

public interface MessageService {
    Message create(Message message);
    List<Message> findByUserId(String userId);
    List<Message> findByMessageContent(String messageContent);
    List<Message> findAll();
    Message update(String userId, Message message);
    void delete(String messageContent);
}
