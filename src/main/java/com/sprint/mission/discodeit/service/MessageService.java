package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.service.message.CreateMessageParam;
import com.sprint.mission.discodeit.dto.service.message.MessageDTO;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageParam;


import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDTO create(CreateMessageParam createMessageParam);
    MessageDTO find(UUID messageId);
    List<MessageDTO> findAllByChannelId(UUID channelId);
    UUID update(UpdateMessageParam updateMessageParam);
    void delete(UUID messageId);
}
