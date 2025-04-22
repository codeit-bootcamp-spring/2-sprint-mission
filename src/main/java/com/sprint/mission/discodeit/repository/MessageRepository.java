package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  Slice<Message> findAllByChannelId(UUID channelId, Pageable pageable);

  // 이 부분도 N + 1 문제를 유발하는 것 같은데 어떻게 수정해야될지 모르겠다
  Slice<Message> findAllByChannelIdAndCreatedAtLessThan(UUID channelId, Instant cratedAt,
      Pageable pageable);
  
  // 이 부분도 N + 1 문제를 유발하는 것 같은데 어떻게 수정해야될지 모르겠다
  Optional<Message> findFirstByChannelIdOrderByCreatedAtDesc(UUID channelId);

  void deleteAllByChannelId(UUID channelId);
}
