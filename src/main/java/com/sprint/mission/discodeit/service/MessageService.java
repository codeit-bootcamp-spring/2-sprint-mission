package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.FindMessageByChannelIdResponseDto;
import com.sprint.mission.discodeit.dto.SaveBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.SaveMessageRequestDto;
import com.sprint.mission.discodeit.dto.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void sendMessage(SaveMessageRequestDto saveMessageRequestDto, List<SaveBinaryContentRequestDto> saveBinaryContentRequestDtoList);
    Message findMessageById(UUID messageId);
    List<Message> findAllMessages();
    List<FindMessageByChannelIdResponseDto> findMessageByChannelId(UUID id);
    void updateMessage(UUID messageId, UpdateMessageRequestDto updateMessageRequestDto, List<SaveBinaryContentRequestDto> saveBinaryContentRequestDtoList);
    void deleteMessageById(UUID id);
}
