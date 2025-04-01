package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;

public interface MessageRepository {


    Message save(Message message);
    List<Message> load();
    void remove(Message message);


}
