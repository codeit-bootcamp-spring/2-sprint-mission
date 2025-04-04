package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.FindMessageByChannelIdResponseDto;
import com.sprint.mission.discodeit.dto.SaveBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {

  void sendMessage(MessageCreateRequest messageCreateRequest,
      List<SaveBinaryContentRequestDto> saveBinaryContentRequestDtoList);

  Message findMessageById(UUID messageId);

  List<Message> findAllMessages();

  List<FindMessageByChannelIdResponseDto> findMessageByChannelId(UUID id);

  void updateMessage(UUID messageId, MessageUpdateRequest messageUpdateRequest);

  void deleteMessageById(UUID id);
}
