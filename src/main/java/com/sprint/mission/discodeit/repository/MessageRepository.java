package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.message.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {

  Message save(Message message);

  Optional<Message> findById(UUID id);

  Optional<Message> findLatestMessageByChannelId(UUID channelId);

  List<Message> findAll();

  List<Message> findAllByChannelId(UUID channelId);

  boolean existsById(UUID id);

  void deleteById(UUID id);

  void deleteByChannelId(UUID channelId);
}
