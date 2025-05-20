package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.PageResponse;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;

import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface MessageService {


    @Transactional
    MessageDto create(MessageCreateRequest messageCreateRequest,
        List<MultipartFile> attachments);

    MessageDto find(UUID messageId);


    @Transactional(readOnly = true)
    PageResponse<MessageDto> findAllByChannelId(UUID channelId, String cursor, int size);

    Instant lastMessageTime(UUID channelId);

    MessageDto update(UUID messageId, MessageUpdateRequest request);

    void delete(UUID messageId);
}
