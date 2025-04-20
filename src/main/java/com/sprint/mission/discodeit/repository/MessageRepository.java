package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {

  Message save(Message message);

  Optional<Message> findById(UUID id);

  List<Message> findAllByChannelId(UUID channelId);

  Page<Message> findAllByChannelIdPaging(UUID channelId, Pageable pageable);

  boolean existsById(UUID id);

  void deleteById(UUID id);

  void deleteAllByChannelId(UUID channelId);
}
