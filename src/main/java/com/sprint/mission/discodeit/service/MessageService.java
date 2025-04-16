package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.messagedto.MessageCreateDto;
import com.sprint.mission.discodeit.service.dto.messagedto.MessageResponseDto;
import com.sprint.mission.discodeit.service.dto.messagedto.MessageUpdateDto;

import java.util.List;
import java.util.UUID;


public interface MessageService {
    MessageResponseDto create(MessageCreateDto messageCreateDto, List<BinaryContentCreateDto>binaryContentCreateDtoList);
    MessageResponseDto find(UUID messageId);
    List<MessageResponseDto> findAllByChannelId(UUID channelId);
    MessageResponseDto update(UUID messageId, MessageUpdateDto messageUpdateDto);
    void delete(UUID messageId);
}
