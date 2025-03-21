package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    MessageResponse create(MessageCreateRequest request);
    Optional<MessageResponse> find(UUID messageId);
    List<MessageResponse> findAllByChannelId(UUID channelId);
    void update(MessageUpdateRequest request);
    void delete(UUID messageId);
}
