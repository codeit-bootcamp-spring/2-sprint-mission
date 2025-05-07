package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  Optional<Message> findTopByChannel_IdOrderByCreatedAtDesc(UUID channelId);

  List<Message> findAllByChannel_Id(UUID channelId);

  Slice<Message> findAllByChannel_Id(UUID channelId, Pageable pageable);

  void deleteAllByChannel_Id(UUID channelId);

  Slice<Message> findAllByChannel_IdOrderByCreatedAtDesc(UUID channelId,
      org.springframework.data.domain.Pageable pageable);
}
