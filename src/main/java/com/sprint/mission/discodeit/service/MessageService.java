package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.FindMessageByChannelIdResponseDto;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {

  void sendMessage(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequestList);

  Message findMessageById(UUID messageId);

  List<Message> findAllMessages();

  List<FindMessageByChannelIdResponseDto> findMessageByChannelId(UUID id);

  void updateMessage(UUID messageId, MessageUpdateRequest messageUpdateRequest);

  void deleteMessageById(UUID id);
}
