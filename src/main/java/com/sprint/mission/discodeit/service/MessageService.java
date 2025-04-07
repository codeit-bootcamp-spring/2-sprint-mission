package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.service.dto.message.MessageUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface MessageService {

  Message create(MessageCreateRequest createRequest,
      List<BinaryContentCreateRequest> binaryContentRequestList);

  Message find(UUID messageId);

  List<Message> findAllByChannelId(UUID channelId);

  Message update(UUID id, MessageUpdateRequest updateRequest);

  void delete(UUID messageId);
}