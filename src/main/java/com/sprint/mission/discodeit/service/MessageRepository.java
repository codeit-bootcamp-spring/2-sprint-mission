package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.*;
import java.util.ArrayList;

public interface MessageRepository {
    //메세지 저장
    void addMessage(Message m);
    //보낸 전체메세지 조회
    List<Message> getMessages(Message m);
    //특정인물에게 보낸 메세지들을 조회
    List<Message> allMessages();
    List<Message> deleteMessages(Message m);


}
