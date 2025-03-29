package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.MessageService.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageService.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(MessageCreateRequest messageCreateRequest, List<BinaryContentDTO> binaryContentDtos);
    List<Message> findByUser(UUID userId);
    List<Message> findByChannel(UUID channelId);
    List<Message> findByUserAndByChannel(UUID userId, UUID channelId);
    List<Message> findAll();
    Message update(MessageUpdateRequest messageUpdateRequest);
    void delete(UUID messageId);
}
