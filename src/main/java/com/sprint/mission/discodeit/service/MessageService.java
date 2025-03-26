package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.CreateMessageDto;
import com.sprint.mission.discodeit.dto.UpdateMessageDto;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(CreateMessageDto dto);
    Message read(UUID channelKey);
    List<Message> readAllByChannelKey(UUID channelKey);
    UpdateMessageDto update(UpdateMessageDto dto);
    void delete(UUID messageKey);
}
