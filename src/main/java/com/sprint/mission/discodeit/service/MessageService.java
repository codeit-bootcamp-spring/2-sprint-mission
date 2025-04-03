package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.controller.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.controller.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.controller.dto.MessageUpdateRequest;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {

  Message create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests);

  Message find(UUID messageId);

  List<Message> findAllBygetChannelId(UUID getChannelId);

  Message update(UUID messageId, MessageUpdateRequest request);

  void delete(UUID messageId);
}
