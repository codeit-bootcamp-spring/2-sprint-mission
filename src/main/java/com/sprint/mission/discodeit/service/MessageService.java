package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.controller.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.service.dto.message.MessageUpdateRequest;
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