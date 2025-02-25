package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;




public interface MessageService {
    void createMessage(Message message);
    void getMessage(String sender);
    void getAllMessage();
    void updateMessage(String sender, String changeMessage);
    void deleteMessage(String sender);
}
