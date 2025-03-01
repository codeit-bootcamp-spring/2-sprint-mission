package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    UUID create(String content, UUID userUuid, UUID channelUuid);
    Message read(UUID channelUuid);
    List<Message> readAll(UUID channelUuid);
    void update(int num, String content);
    void delete(int num);
}
