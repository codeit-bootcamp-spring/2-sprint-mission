package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {

  void saveData();

  void register(Message message);

  Optional<Message> findById(UUID id);

  List<Message> findAll();

  List<Message> findAllByChannelId(UUID channelId);

  List<Message> findAllByAuthorId(UUID authorId);

  List<ZonedDateTime> findTimestampsByChannelIdSorted(UUID channelId);

  Optional<ZonedDateTime> findLatestTimestampByChannelId(UUID channelId);

  boolean updateMessage(Message message);

  boolean deleteMessage(UUID id);
}
