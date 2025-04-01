package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import java.util.List;
import java.util.UUID;

public interface MessageService {

    UUID create(CreateMessageRequest request);

    UUID create(CreateMessageRequest request,
        List<CreateBinaryContentRequest> binaryContents);

    MessageResponseDto findById(UUID messageId);

    List<MessageResponseDto> findAll();

    List<MessageResponseDto> findByChannelId(UUID channelId);

    void update(UUID messageId, UpdateMessageRequest request);

    void remove(UUID messageId);
}
