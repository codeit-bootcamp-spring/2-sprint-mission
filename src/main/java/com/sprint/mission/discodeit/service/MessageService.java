package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.messagedto.*;

import java.util.List;
import java.util.UUID;


public interface MessageService {
    Message create(MessageCreateDto messageCreateDto, List<BinaryContentCreateDto>binaryContentCreateDtoList);
    MessageFindResponseDto find(MessageFindRequestDto messageFindRequestDto);
    List<Message> findAllByChannelId(UUID channelId);
    Message update(UUID messageId, MessageUpdateDto messageUpdateDto);
    void delete(UUID messageId);
}
