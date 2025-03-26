package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.common.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.message.response.MessageCreateResponse;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageCreateResponse create(MessageCreateRequest request);
    Message find(UUID messageId);
    List<Message> findAllByChannelId(UUID channelId);
    MessageUpdateDto update(MessageUpdateDto request);
    void delete(UUID messageId);
}
