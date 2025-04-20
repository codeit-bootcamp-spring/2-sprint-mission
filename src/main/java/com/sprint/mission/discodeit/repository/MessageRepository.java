package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.message.Message;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  Optional<Message> findTop1ByChannelOrderByCreatedAtDesc(Channel channel);

  Slice<Message> findByChannel_Id(UUID channelId, Pageable pageable);

  void deleteByChannel_Id(UUID channelId);
}
