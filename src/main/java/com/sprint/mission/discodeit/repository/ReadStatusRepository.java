package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;

import com.sprint.mission.discodeit.entity.User;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  boolean existsByUserAndChannel(User user, Channel channel);

  List<ReadStatus> findAllByUser(User user);

  List<ReadStatus> findAllByChannel_Id(UUID channelId);

  List<Channel> findAllByUser_Id(UUID userId);

  void deleteAllByChannel_Id(UUID channelId);
}
