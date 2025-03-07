package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    Message messageSave(UUID channelUUID, UUID userUUID , String content);
    Optional<Message> findMessageById(UUID messageUUID);
    List<Message> findAllMessage();
    List<Message> findMessageByChannel(UUID channelUUID);
    Message updateMessage(UUID messageUUID, String content);
    boolean deleteMessageById(UUID messageUUID);
}
