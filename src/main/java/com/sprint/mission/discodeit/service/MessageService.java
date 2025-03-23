package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.MessageDTO;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(MessageDTO messageDTO);
    Message getMessage(UUID id);
    List<Message> getAllMessages();

    void deleteMessage(UUID id);
    void updateMessage(UUID id, String newMessageName);
}
