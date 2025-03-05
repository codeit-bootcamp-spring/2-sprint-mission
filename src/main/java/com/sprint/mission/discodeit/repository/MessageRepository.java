package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    void save(Message message); // 메시지 저장
    Message findById(UUID messageId); // 메시지 조회
    List<Message> findAll(); // 모든 메시지 조회
    void delete(UUID messageId); // 메시지 삭제
    boolean exists(UUID messageId);
}
