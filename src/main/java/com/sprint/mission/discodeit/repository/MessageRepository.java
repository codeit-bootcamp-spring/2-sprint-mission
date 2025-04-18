package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.message.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  Optional<Message> findTop1ByChannelOrderByCreatedAtDesc(Channel channel);

  List<Message> findAllByChannel_Id(UUID channelId);

  void deleteByChannel_Id(UUID channelId);
}
