package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public interface MessageService {
    // CRUD(생성, 읽기, 모두 읽기, 수정, 삭제) 기능
    Message createMessage(UUID userId, UUID channelId, String message); // 메세지 전송 (누가, 어느 톡장에, 어떤 내용)
    void getMessageInfo(UUID channelId); //특정 톡방의 정보
    void getAllMessageData(); // 모든 메세지 정보 출력 (누구, 어디에 상관없이)
    void updateMessage(UUID messageId, String newMessageContent); // 특정 메세지 내용 수정
    void deleteMessage(UUID messageId); //메세지 삭제
}
