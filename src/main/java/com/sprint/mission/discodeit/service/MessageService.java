package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDto create(MessageCreateDto messageCreateDto, List<BinaryContentCreateDto> binaryContentCreateDtos);

    MessageDto findById(UUID messageId);

    List<MessageDto> findAllByChannelId(UUID channelId);

    List<MessageDto> findAllByAuthorId(UUID authorId);

    MessageDto update(UUID messageId, MessageUpdateDto messageUpdateDto);

    void delete(UUID messageId);
}
