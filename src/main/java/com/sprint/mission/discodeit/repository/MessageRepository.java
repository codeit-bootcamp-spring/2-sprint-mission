package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);
    void delete(UUID messageKey);
    Message findByKey(UUID messageKey);
    Message findByChannelKey(UUID channelKey);
    List<Message> findAllByChannelKey(UUID channelKey);
    UUID findKeyByMessageId(int messageId);
}
