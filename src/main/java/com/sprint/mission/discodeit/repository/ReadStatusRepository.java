package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  @Query("select distinct rs from ReadStatus rs"
      + " join fetch rs.user"
      + " join fetch rs.channel"
      + " where rs.user.id = :userId")
  List<ReadStatus> findAllByUserId(@Param("userId") UUID userId);

  @Query("select distinct rs from ReadStatus rs"
      + " join fetch rs.user"
      + " join fetch rs.channel"
      + " where rs.channel.id = :channelId")
  List<ReadStatus> findAllByChannelId(@Param("channelId") UUID channelId);

  void deleteByChannelId(UUID channelId);

  Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);


}
