package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.messagedto.*;

import java.util.List;


public interface MessageService {
    Message create(MessageCreateDto messageCreateDto, List<BinaryContentCreateDto>binaryContentCreateDtoList);
    MessageFindResponseDto find(MessageFindRequestDto messageFindRequestDto);
    List<MessageFindAllByChannelIdResponseDto> findAllByChannelId(MessageFindAllByChannelIdRequestDto messageFindAllByChannelIdRequestDto);
    Message update(MessageUpdateDto messageUpdateDto);
    void delete(MessageDeleteDto messageDeleteDto);
}
