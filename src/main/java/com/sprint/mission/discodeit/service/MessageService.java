package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.message.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(MessageCreateRequest messageCreateRequest);
    Message find(UUID messageId);
    List<Message> findAllByChannelId(UUID channelId);
    Message update(MessageUpdateRequest requestDto);
    void delete(UUID messageId);
}
