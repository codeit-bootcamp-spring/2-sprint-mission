package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MessageService {

    MessageDto create(CreateMessageRequest request);

    MessageDto create(CreateMessageRequest request,
        List<CreateBinaryContentRequest> binaryContents);

    MessageDto findById(UUID messageId);

    PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant cursor, int size);

    MessageDto update(UUID messageId, UpdateMessageRequest request);

    void remove(UUID messageId);
}
