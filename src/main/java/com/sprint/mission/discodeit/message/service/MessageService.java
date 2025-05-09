package com.sprint.mission.discodeit.message.service;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.common.dto.response.PageResponse;
import com.sprint.mission.discodeit.message.dto.MessageResult;
import com.sprint.mission.discodeit.message.dto.request.MessageByChannelRequest;
import com.sprint.mission.discodeit.message.dto.request.MessageCreateRequest;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    MessageResult create(MessageCreateRequest messageCreateRequest, List<BinaryContentRequest> profileImages);

    MessageResult getById(UUID id);

    PageResponse<MessageResult> getAllByChannelId(MessageByChannelRequest messageByChannelRequest);

    MessageResult updateContext(UUID id, String context);

    void delete(UUID id);
}
