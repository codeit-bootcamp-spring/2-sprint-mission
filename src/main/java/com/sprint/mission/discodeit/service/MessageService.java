package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(MessageCreateDto messageCreateDto);

    Message findById(UUID messageId);

    List<Message> findAllByChannelId(UUID channelId);

    List<Message> findAllByAuthorId(UUID authorId);

    Message update(UUID messageId, String newContent);

    void delete(UUID messageId);
}
