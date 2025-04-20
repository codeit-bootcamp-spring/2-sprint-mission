package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.PageResponse;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;


import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface MessageService {


    @Transactional
    MessageDto create(MessageCreateRequest messageCreateRequest,
        List<BinaryContentCreateRequest> binaryContentCreateRequests);

    Message find(UUID messageId);


    @Transactional(readOnly = true)
    PageResponse<MessageDto> findAllByChannelId(UUID channelId, Pageable pageable);

    Instant lastMessageTime(UUID channelId);

    Message update(UUID messageId, MessageUpdateRequest request);

    void delete(UUID messageId);
}
