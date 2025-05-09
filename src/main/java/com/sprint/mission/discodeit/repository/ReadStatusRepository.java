package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.common.ReadStatus;

import com.sprint.mission.discodeit.entity.user.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  Optional<ReadStatus> findByUserAndChannel(User user, Channel channel);

  void deleteByChannel_Id(UUID channelId);

  List<ReadStatus> findAllByUser_Id(UUID userId);

  @Query("select rs.user.id from ReadStatus rs where rs.channel = :channel")
  List<UUID> findUserIdsByChannel(@Param("channel") Channel channel);
}
