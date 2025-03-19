package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    void save();

    void addMessage(Message message); // 메시지 저장

    Message findMessageById(UUID messageId); // 메시지 조회

    List<Message> findMessageAll(); // 모든 메시지 조회

    void deleteMessageById(UUID messageId); // 메시지 삭제

    boolean existsById(UUID messageId);

    void deleteMessageByChannelId(UUID channelId);

    Message findLatestMessageByChannelId(UUID channelId);
}
