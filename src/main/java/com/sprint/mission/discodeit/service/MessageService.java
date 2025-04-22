package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResult;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    MessageResult create(MessageCreateRequest messageCreateRequest, List<BinaryContentRequest> profileImages);

    MessageResult getById(UUID id);

    PageResponse<MessageResult> getAllByChannelId(UUID channelId, Pageable pageable);

    MessageResult updateContext(UUID id, String context);

    void delete(UUID id);
}
