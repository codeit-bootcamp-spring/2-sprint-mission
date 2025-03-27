package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(MessageCreateRequest request);
    Message read(UUID channelKey);
    List<Message> readAllByChannelKey(UUID channelKey);
    Message update(MessageUpdateRequest request);
    void delete(UUID messageKey);
}
