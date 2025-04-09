package com.sprint.discodeit.sprint5.service.basic.message;

import com.sprint.discodeit.sprint5.domain.dto.messageDto.MessageRequestDto;
import com.sprint.discodeit.sprint5.domain.dto.messageDto.MessageUpdateRequestDto;
import com.sprint.discodeit.sprint5.domain.entity.Message;
import java.util.List;
import java.util.UUID;

public interface MessageServiceV1 {


    Message create(UUID channelId, MessageRequestDto messageRequestDto);
    Message find(UUID messageId);
    List<Message> findAllByChannelId(UUID channelId);
    Message update(UUID messageId, MessageUpdateRequestDto messageUpdateRequestDto);
    void delete(UUID messageId);
}
