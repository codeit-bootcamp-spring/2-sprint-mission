package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    Message createMessage(UUID userId, UUID channelId, String text);  // 메시지 생성
    Optional<Message> getMessageById(UUID messageId);  // 특정 메시지를 메시지ID로 조회
    List<Message> getAllMessagesByChannel(UUID channelId); // 특정 채널의 메시지를 채널ID로 조회
    void updateMessage(UUID messageId, String newText);  // 메시지 수정
    void deleteMessage(UUID messageId);
}
