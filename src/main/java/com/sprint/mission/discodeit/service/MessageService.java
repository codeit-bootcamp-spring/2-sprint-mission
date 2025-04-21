package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDto create(MessageCreateRequest messageCreateRequest, List<BinaryContentCreateRequest> binaryContentCreateRequests);

    Message searchMessage(UUID messageId);

    PageResponse<MessageDto> findAllByChannelId(UUID channelId, Pageable pageable);

    MessageDto updateMessage(UUID messageId, MessageUpdateRequest dto);

    void deleteMessage(UUID messageId);
}
