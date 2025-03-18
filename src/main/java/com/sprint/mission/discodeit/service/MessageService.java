package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.messageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.service.messageDto.MessageResponse;
import com.sprint.mission.discodeit.service.messageDto.MessageUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(MessageCreateRequest request);
    MessageResponse find(UUID messageId);
    List<MessageResponse> findAllByChannelId(UUID channelId);
    MessageResponse update(MessageUpdateRequest request);
    void delete(UUID messageId);
}
