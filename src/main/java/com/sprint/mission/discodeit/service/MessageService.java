package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MessageService {

  MessageDto create(MessageCreateRequest messageRequest,
      List<BinaryContentCreateRequest> attachmentRequests);

  MessageDto read(UUID channelId);

  Slice<MessageDto> readAllByChannelId(UUID channelId, Pageable pageable);

  MessageDto update(UUID messageId, MessageUpdateRequest messageRequest);

  void delete(UUID messageId);
}
