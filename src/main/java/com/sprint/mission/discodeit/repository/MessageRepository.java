package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  Optional<Message> findTopByChannelIdOrderByCreatedAtDesc(UUID channelId);

  List<Message> findAllByChannelIdAndAuthorId(UUID channelId, UUID authorId);

  List<Message> findAllByAuthorId(UUID authorId);

  List<Message> findAllByChannelId(UUID channelId);

  void deleteAllByChannelId(UUID channelId);


}
