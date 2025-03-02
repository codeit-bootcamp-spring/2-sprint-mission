package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void create(Message message);
    Message find(UUID id);
    List<Message> findAll();
    void update(Message message);
    void delete(UUID id);
}
