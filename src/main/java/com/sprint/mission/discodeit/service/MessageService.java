package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.FindMessageByChannelIdResponseDto;
import com.sprint.mission.discodeit.dto.SaveMessageParamDto;
import com.sprint.mission.discodeit.dto.UpdateMessageParamDto;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    void sendMessage(SaveMessageParamDto saveMessageParamDto);
    Message findMessageById(UUID id);
    List<Message> findAllMessages();
    List<FindMessageByChannelIdResponseDto> findMessageByChannelId(UUID id);
    void updateMessage(UpdateMessageParamDto updateMessageParamDto);
    void deleteMessageById(UUID id);
}
