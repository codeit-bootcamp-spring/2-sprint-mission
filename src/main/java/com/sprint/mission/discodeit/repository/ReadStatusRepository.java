package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.common.ReadStatus;

import com.sprint.mission.discodeit.entity.user.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  Optional<ReadStatus> findByUserAndChannel(User user, Channel channel);

  void deleteByChannel_Id(UUID channelId);

  List<ReadStatus> findAllByChannel(Channel channel);

  List<ReadStatus> findAllByUser_Id(UUID userId);
}
