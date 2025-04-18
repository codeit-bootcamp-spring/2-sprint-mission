package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface MessageService {

  MessageResponse create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> attachmentsCreateRequest);

  MessageResponse find(UUID messageId);

  List<MessageResponse> findAllByChannelId(UUID channelId);

  MessageResponse update(UUID messageId, MessageUpdateRequest request);

  void delete(UUID messageId);
}
