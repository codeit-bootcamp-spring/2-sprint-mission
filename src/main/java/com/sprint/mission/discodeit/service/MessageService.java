package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    Message create(MessageDto messageDto, List<BinaryContentDto> binaryContentDto);
    Message findById(UUID messageId);
    List<Message> findAllByChannelId(UUID channelId);
    Message update(UUID messageId, MessageUpdateDto messageUpdateDto);
    void delete(UUID messageId);
}
