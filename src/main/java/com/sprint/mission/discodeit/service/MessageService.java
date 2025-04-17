package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.service.dto.request.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.request.messagedto.MessageCreateDto;
import com.sprint.mission.discodeit.service.dto.request.messagedto.MessageUpdateDto;
import com.sprint.mission.discodeit.service.dto.response.MessageResponseDto;
import com.sprint.mission.discodeit.service.dto.response.PageableResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;


public interface MessageService {
    MessageResponseDto create(MessageCreateDto messageCreateDto, List<BinaryContentCreateDto>binaryContentCreateDtoList);
    PageableResponseDto<MessageResponseDto> find(UUID messageId, int page, int size);
    PageableResponseDto<MessageResponseDto> findAllByChannelId(UUID channelId, Pageable pageable);
    MessageResponseDto update(UUID messageId, MessageUpdateDto messageUpdateDto);
    void delete(UUID messageId);
}
