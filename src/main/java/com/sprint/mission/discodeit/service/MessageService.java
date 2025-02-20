package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    // 메시지 생성
    void createMessage(Message message);

    // 메시지 읽기
    // UUID id를 파라미터로 사용해 불러오기엔, UUID는 난수값이라 미리 알기가 힘들고 Message DB를 따로 사용하지 않으므로 알길이 없음
    // => content 으로 Message를 찾는게 훨씬 편할 것 같다.
    Message getMessage(String content);

    // 메시지 모두 읽기
    List<Message> getAllMessages();

    // 메시지 수정
    void updateMessage(Message message);

    // 메시지 삭제
    void deleteMessage(String content);
}
