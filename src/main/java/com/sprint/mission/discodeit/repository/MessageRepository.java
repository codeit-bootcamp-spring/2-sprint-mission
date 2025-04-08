package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);
    Message findByKey(UUID messageKey);
    List<Message> findAll();
    List<Message> findAllByChannelKey(UUID channelKey);
    void delete(UUID messageKey);
    boolean existsByKey(UUID messageKey);
}
