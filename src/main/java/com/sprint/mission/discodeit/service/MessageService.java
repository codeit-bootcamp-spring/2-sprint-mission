package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;


public interface MessageService {
    Message create(Message message);
    List<Message> getMessage(String sender);
    List<Message> getAllMessage();
    Message update(String sender, UUID uuid , String changeMessage);
    void delete(String sender, UUID uuid);
}
