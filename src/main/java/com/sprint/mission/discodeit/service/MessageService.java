package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.controller.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.service.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.service.message.MessageUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface MessageService {

  MessageDto create(MessageCreateRequest createRequest,
      List<BinaryContentCreateRequest> binaryContentRequestList);

  Message find(UUID messageId);

  List<MessageDto> findAllByChannelId(UUID channelId); // 페이징 추가

  MessageDto update(UUID id, MessageUpdateRequest updateRequest);

  void delete(UUID messageId);
}