package com.sprint.mission.discodeit.core.read.repository;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.read.entity.ReadStatus;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaReadStatusRepository extends JpaRepository<ReadStatus, UUID>,
    CustomReadStatusRepository {

  void deleteAllByChannel(Channel channel);
}
