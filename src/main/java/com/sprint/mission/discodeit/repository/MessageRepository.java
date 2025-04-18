package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface MessageRepository {

  Message save(Message message);

  List<Message> findAll();

  Message findById(UUID messageId);

  List<Message> findAllByChannelId(UUID channelId);

  void delete(UUID messageId);

  Map<UUID, Message> getMessageData();

  Message update(Message message, String newContent);
}
