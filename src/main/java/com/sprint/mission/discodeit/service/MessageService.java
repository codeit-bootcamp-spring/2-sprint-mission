package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
        Message create(String content, UUID channelId, UUID authorId);
        Message findByMessage(UUID messageId);
        List<Message> findAllMessage();
        Message updateMessage(UUID messageId, String newContent);
        boolean deleteMessage(UUID messageId);
    }
