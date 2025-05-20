package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface MessageService {

  MessageResponse create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> attachmentsCreateRequest);

  MessageResponse find(UUID messageId);

  PageResponse<MessageResponse> findAllByChannelId(UUID channelId, Instant createdAt,
      Pageable pageable);

  MessageResponse update(UUID messageId, MessageUpdateRequest request);

  void delete(UUID messageId);
}
