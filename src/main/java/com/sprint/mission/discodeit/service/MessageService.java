package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(MessageCreateDto messageCreateDto, List<BinaryContentCreateDto> binaryContentCreateDtos);

    Message findById(UUID messageId);

    List<Message> findAllByChannelId(UUID channelId);

    List<Message> findAllByAuthorId(UUID authorId);

    Message update(MessageUpdateDto messageUpdateDto);

    void delete(UUID messageId);
}
