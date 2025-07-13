package com.sprint.mission.discodeit.core.message.service;

import com.sprint.mission.discodeit.core.message.dto.MessageDto;
import com.sprint.mission.discodeit.core.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.core.message.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.core.storage.dto.BinaryContentCreateRequest;
import java.util.List;
import java.util.UUID;

public interface MessageService {

  MessageDto create(MessageCreateRequest request,
      List<BinaryContentCreateRequest> binaryContentCommands);

  MessageDto update(UUID messageId, MessageUpdateRequest request);

  void delete(UUID messageId);
}
