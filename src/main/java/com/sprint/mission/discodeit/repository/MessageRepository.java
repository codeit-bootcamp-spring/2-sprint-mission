package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository {

  Message save(Message message);

  Optional<Message> findById(UUID id);

  List<Message> findAllByChannel(Channel channel);

  void delete(Message message);

  void deleteAllByChannel(Channel channel);
}
