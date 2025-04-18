package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;

public interface MessageRepositoryCustom {
  @EntityGraph(attributePaths = {"author", "channel"})
  Slice<Message> findByChannelIdBeforeCursor(UUID channelId, Instant cursor, Pageable pageable);


}
