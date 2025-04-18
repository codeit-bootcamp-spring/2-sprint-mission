package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);

  @Query("select rs.user from ReadStatus rs where rs.channel.id = :channelId")
  List<User> findAllUserByChannelId(@Param("channelId") UUID channelId);

  List<ReadStatus> findAllByUserId(UUID userId);

  @Query("select rs.channel.id from ReadStatus rs where rs.user.id = :userId")
  List<UUID> findAllChannelIdByUserId(@Param("userId") UUID userId);

  void deleteAllByChannelId(UUID channelId);
}
