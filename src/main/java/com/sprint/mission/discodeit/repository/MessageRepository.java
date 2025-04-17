package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    Slice<Message> findAllByChannelId(UUID channelId, Pageable pageable);

    List<Message> findAllByChannelId(UUID channelId);

    Slice<Message> findAllByAuthorId(UUID authorId, Pageable pageable);

    List<Message> findAllByAuthorId(UUID authorId);
}
