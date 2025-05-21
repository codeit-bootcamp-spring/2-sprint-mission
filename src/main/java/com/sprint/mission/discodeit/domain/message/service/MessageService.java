package com.sprint.mission.discodeit.domain.message.service;

import com.sprint.mission.discodeit.common.dto.response.PageResponse;
import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.message.dto.MessageResult;
import com.sprint.mission.discodeit.domain.message.dto.request.ChannelMessagePageRequest;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageCreateRequest;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    MessageResult create(MessageCreateRequest messageCreateRequest, List<BinaryContentRequest> profileImages);

    MessageResult getById(UUID id);

    PageResponse<MessageResult> getAllByChannelId(UUID channelId, ChannelMessagePageRequest messageByChannelRequest);

    MessageResult updateContext(UUID id, String context);

    void delete(UUID id);
}
