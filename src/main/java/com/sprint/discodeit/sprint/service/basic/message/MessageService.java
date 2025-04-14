package com.sprint.discodeit.sprint.service.basic.message;

import com.sprint.discodeit.sprint.domain.dto.messageDto.MessageRequestDto;
import com.sprint.discodeit.sprint.domain.dto.messageDto.MessageUpdateRequestDto;
import com.sprint.discodeit.sprint.domain.entity.Message;
import java.util.List;
import java.util.UUID;

public interface MessageService {


    Message create(Long channelId, MessageRequestDto messageRequestDto);
    Message find(Long messageId);
    List<Message> findAllByChannelId(Long channelId);
    Message update(Long messageId, MessageUpdateRequestDto messageUpdateRequestDto);
    void delete(Long messageId);
}
