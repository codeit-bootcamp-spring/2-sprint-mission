package com.sprint.mission.discodeit.core.message.repository;

import com.sprint.mission.discodeit.core.message.dto.PageResponse;
import com.sprint.mission.discodeit.core.message.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface CustomMessageRepository {

  Message findByMessageId(UUID id);

  PageResponse<Message> findAllByChannelIdWithAuthor(UUID channelId, Instant cursor,
      Pageable pageable);

  List<Message> findByChannelId(UUID channelId);

}
