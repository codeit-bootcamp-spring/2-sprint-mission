package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.CreateMessageDTO;
import com.sprint.mission.discodeit.dto.MessageResponseDTO;
import com.sprint.mission.discodeit.dto.UpdateMessageDTO;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageResponseDTO create(CreateMessageDTO createMessageDTO);
    List<MessageResponseDTO> findAllByChannelId(UUID channelId);
    MessageResponseDTO find(UUID messageId);
    void update(UpdateMessageDTO updateMessageDTO);
    void delete(UUID messageId);
}
