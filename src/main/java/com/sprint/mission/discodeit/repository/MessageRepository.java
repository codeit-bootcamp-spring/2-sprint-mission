package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    UUID createMessage(Message message);
    Message findById(UUID id);
    List<Message> findAllByChannelId(UUID channelId);
    void updateMessage(UUID id, String content, UUID userId, UUID channelId, List<UUID> attachmentIds);
    void deleteMessage(UUID id, UUID userId, UUID channelId);
    Optional<Instant> findLatestCreatedAtByChannelId(UUID channelId);
    void deleteMessageByChannelId(UUID channelId);

}
