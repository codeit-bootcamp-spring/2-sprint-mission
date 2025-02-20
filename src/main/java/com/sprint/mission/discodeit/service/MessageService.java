package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void sendMessage(User user, Channel channel, String content);
    void findMessageById(UUID id);
    void findAllMessages();
    void updateMessage(UUID id, String content);
    void deleteMessageById(UUID id);
}
