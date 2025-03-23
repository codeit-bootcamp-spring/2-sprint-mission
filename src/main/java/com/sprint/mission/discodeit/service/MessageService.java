package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.CreateMessageDTO;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDTO;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(CreateMessageDTO dto);

    Message searchMessage(UUID messageId);

    List<Message> searchAllByChannelId(UUID channelId);

    Message updateMessage(UpdateMessageDTO dto);

    void deleteMessage(UUID messageId);
}
