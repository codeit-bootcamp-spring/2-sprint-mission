package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(MessageCreateRequestDto requestDto);
    Message find(UUID messageId);
    List<Message> findAllByChannelId(UUID channelId);
    Message update(MessageUpdateRequestDto updateRequestDto);
    void delete(UUID messageId);
}
