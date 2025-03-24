package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreate;
import com.sprint.mission.discodeit.dto.message.MessageUpdate;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(MessageCreate dto);
    Message find(UUID messageId);
    List<Message> findAllByChannelId(UUID channelId);
    Message update(MessageUpdate dto);
    void delete(UUID messageId);
}
