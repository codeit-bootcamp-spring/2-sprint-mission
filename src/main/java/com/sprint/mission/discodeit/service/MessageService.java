package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface MessageService {
    MessageDto create(MessageCreateDto messageCreateDto, List<BinaryContentCreateDto> binaryContentCreateDtos);

    MessageDto findById(UUID messageId);

    PageResponse<MessageDto> findAllByChannelId(UUID channelId, Pageable pageable);

    PageResponse<MessageDto> findAllByAuthorId(UUID authorId, Pageable pageable);

    MessageDto update(UUID messageId, MessageUpdateDto messageUpdateDto);

    void delete(UUID messageId);
}
