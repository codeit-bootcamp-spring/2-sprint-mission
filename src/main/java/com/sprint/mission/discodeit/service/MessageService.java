package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.entity.message.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(MessageCreateRequestDto messageCreateRequestDto);
    Message find(UUID messageId);
    List<Message> findAllByChannelId(UUID channelId);
    Message update(MessageUpdateRequestDto requestDto);
    void delete(UUID messageId);
}
