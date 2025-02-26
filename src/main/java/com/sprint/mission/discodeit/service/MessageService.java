package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

// interface(인터페이스)
// 하위모듈에서 어떤 기능들을 구현할지 선언만 해놓은 공간
// CRUD(생성, 읽기, 수정, 삭제)
// message도 id가 있다.
public interface MessageService {
    // Create - 생성
    boolean CreateMessage(String message, String username, String channelname);
    // Read - 읽기, 조회
    Optional<Message> getMessage(UUID id);
    Map<UUID, Message> getAllMessage();
    // Update - 수정
    void UpdateMessage(String message, UUID id);
    // Delete - 삭제
    void DeleteMessage(UUID id);
}
