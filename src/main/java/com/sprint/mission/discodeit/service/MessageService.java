package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.controller.PageResponse;
import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.service.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.service.message.MessageDto;
import com.sprint.mission.discodeit.dto.service.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface MessageService {

  MessageDto create(MessageCreateRequest createRequest,
      List<BinaryContentCreateRequest> binaryContentRequestList);

  Message find(UUID messageId);

  PageResponse<MessageDto> findAllByChannelId(UUID channelId, Pageable pageable);

  MessageDto update(UUID id, MessageUpdateRequest updateRequest);

  void delete(UUID messageId);
}