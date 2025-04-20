package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID>{
  List<Message> findAllByChannelId(UUID channelId);
  void deleteAllByChannelId(UUID channelId);
}