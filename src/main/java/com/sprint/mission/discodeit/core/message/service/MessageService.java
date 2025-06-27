package com.sprint.mission.discodeit.core.message.service;

import com.sprint.mission.discodeit.core.message.dto.MessageDto;
import com.sprint.mission.discodeit.core.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.core.message.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.core.storage.dto.BinaryContentCreateRequest;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MessageService {

  MessageDto create(MessageCreateRequest request,
      List<BinaryContentCreateRequest> binaryContentCommands);

  List<MessageDto> findByChannelId(UUID channelId);

  Slice<MessageDto> findAllByChannelId(UUID channelId, Instant cursor, Pageable pageable);

  MessageDto update(UUID messageId, MessageUpdateRequest request);

  void delete(UUID messageId);
}
