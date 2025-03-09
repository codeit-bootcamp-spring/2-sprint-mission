package com.sprint.mission.discodeit.service;



import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    //메세지 저장
    void register(Message m);
    //보낸 전체메세지 조회
    List<Message> findAll();
    Optional<Message> findById(UUID messageId);

    boolean deleteMessage(UUID messageId);


}
