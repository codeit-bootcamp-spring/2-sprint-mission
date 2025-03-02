package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

public interface MessageService {
    void sendMessage(Message message);//전송
    void reciveMessage();
    void AllMessageView();//받기
    void deleteMessage(Message message);
}
