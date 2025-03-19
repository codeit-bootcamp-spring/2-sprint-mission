package com.sprint.discodeit.service;

import com.sprint.discodeit.domain.dto.messageDto.MessageRequestDto;
import com.sprint.discodeit.domain.dto.messageDto.MessageUpdateRequestDto;
import com.sprint.discodeit.domain.entity.Message;
import java.util.List;
import java.util.UUID;

public interface MessageServiceV1 {

    Message create(MessageRequestDto messageRequestDto);
    Message find(UUID messageId);
    List<Message> findAllByChannelId(UUID channelId);
    Message update(MessageUpdateRequestDto messageUpdateRequestDto);
    void delete(UUID messageId);
}
