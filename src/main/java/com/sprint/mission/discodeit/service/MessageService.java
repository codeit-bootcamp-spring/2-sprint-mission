package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.dto.messagedto.*;

import java.rmi.server.UID;
import java.util.List;
import java.util.UUID;


public interface MessageService {
    Message create(MessageCreateDto messageCreateDto);
    MessageFindResponseDto find(MessageFindRequestDto messageFindRequestDto);
    List<MessageFindAllByChannelIdResponseDto> findallByChannelId(MessageFindAllByChannelIdRequestDto messageFindAllByChannelIdRequestDto);
    Message update(MessageUpdateDto messageUpdateDto);
    void delete(MessageDeleteDto messageDeleteDto);
}
