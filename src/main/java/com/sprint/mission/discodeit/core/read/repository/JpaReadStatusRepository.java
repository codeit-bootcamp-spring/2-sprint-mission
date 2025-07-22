package com.sprint.mission.discodeit.core.read.repository;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.read.entity.ReadStatus;
import com.sprint.mission.discodeit.core.user.entity.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaReadStatusRepository extends JpaRepository<ReadStatus, UUID>,
    CustomReadStatusRepository {

  void deleteAllByChannel(Channel channel);

  boolean existsByUserAndChannel(User user, Channel channel);
}
