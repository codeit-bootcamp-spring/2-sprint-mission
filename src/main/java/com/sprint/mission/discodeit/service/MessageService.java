package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.request.message.MessageByChannelRequest;
import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.dto.service.message.MessageResult;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    MessageResult create(MessageCreateRequest messageCreateRequest, List<BinaryContentRequest> profileImages);

    MessageResult getById(UUID id);

    PageResponse<MessageResult> getAllByChannelId(MessageByChannelRequest messageByChannelRequest);

    MessageResult updateContext(UUID id, String context);

    void delete(UUID id);
}
