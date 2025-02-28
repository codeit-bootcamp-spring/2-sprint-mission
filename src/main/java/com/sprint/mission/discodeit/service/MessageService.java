package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.ChannelEntity;
import com.sprint.mission.discodeit.entity.MessageEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService extends BaseService<MessageEntity> {
    MessageEntity createMessage(String content, UUID senderId, ChannelEntity channel);
    Optional<MessageEntity> getMessageById(UUID messageId);
    List<MessageEntity> getAllMessages();
    void updateMessage(UUID messageId, String newContent);
}

