package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    boolean sendMessage(Message message);
    boolean deleteMessage(UUID messageId);
//    List<String> findAllMessages();
    List<Message> findAllMessages();
    String createAllMessageContents();
    Message findOneMessage(UUID messageId);
    void editMessage(UUID messageId, String content);
    String displayEditmessages();
    List<Message> findEditedMessages();
}
