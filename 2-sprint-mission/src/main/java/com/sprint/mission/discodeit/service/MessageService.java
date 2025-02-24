package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    //생성
    void createMessage(String message);

    //읽기
    Message getMessageById(UUID id);

    //모두읽기
    List<Message> getAllMessages();

    //수정
    Message updateMessage(UUID id, String message);

    //삭제
    void deleteMessage(UUID id);
}
