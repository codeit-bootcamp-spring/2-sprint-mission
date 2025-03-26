package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.CreateMessageReqDto;
import com.sprint.mission.discodeit.dto.message.MessageResDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageReqDto;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageResDto create(CreateMessageReqDto createMessageReqDto);
    MessageResDto find(UUID messageId);
    List<MessageResDto> findAllByChannelId(UUID channelId);
    void update(UUID messageId, UpdateMessageReqDto updateMessageReqDto);
    void delete(UUID messageId);
}
