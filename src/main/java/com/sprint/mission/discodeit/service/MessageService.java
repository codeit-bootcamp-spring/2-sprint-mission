package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// interface(인터페이스)
// 하위모듈에서 어떤 기능들을 구현할지 선언만 해놓은 공간
// CRUD(생성, 읽기, 수정, 삭제)
// message도 id가 있다.
public interface MessageService {
    // Create - 생성
    void createMessage(String message, UUID userid, UUID channelid);
    // Read - 읽기, 조회
    Optional<Message> getOneMessage(UUID id);
    List<Message> getAllMessage();
    // Update - 수정
    void updateMessage(String message, UUID id);
    // Delete - 삭제
    void deleteMessage(UUID id);
}
