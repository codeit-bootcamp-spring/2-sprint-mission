package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends Repository<Message> {
    void addChannelIdToChannelIdMessage(UUID channelId);
    List<Message> findMessageListByChannelId(UUID channelId);
    void updateMessageContent(UUID messageId, String newContent);
    void updateAttachmentIds(UUID messageId, List<UUID> attachmentIds);
}
