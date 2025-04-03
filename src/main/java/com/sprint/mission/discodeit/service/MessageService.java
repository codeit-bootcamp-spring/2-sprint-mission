package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.controller.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.controller.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.controller.dto.MessageUpdateRequest;

import com.sprint.mission.discodeit.entity._Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {

  _Message create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests);

  _Message find(UUID messageId);

  List<_Message> findAllBygetChannelId(UUID getChannelId);

  _Message update(UUID messageId, MessageUpdateRequest request);

  void delete(UUID messageId);
}
