package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ReadStatusJPARepository extends JpaRepository<ReadStatus, UUID> {
    Boolean existsByUser_IdAndChannel_Id(UUID userId, UUID channelId);

    @Query("select rs from ReadStatus rs where rs.user.id = :userId")
    @EntityGraph(attributePaths = {"user", "channel"})
    List<ReadStatus> findByUser_Id(@Param("userId") UUID userId);

    @Query("select rs from ReadStatus rs where rs.channel.id = :channelId")
    @EntityGraph(attributePaths = {"user", "channel"})
    List<ReadStatus> findByChannel_Id(@Param("channelId") UUID channelId);
}
