package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.Locked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ReadStatusJPARepository extends JpaRepository<ReadStatus, UUID> {
    Boolean existsByUser_IdAndChannel_Id(UUID userId, UUID channelId);
    List<ReadStatus> findByUser_Id(UUID userId);
    List<ReadStatus> findByChannel_Id(UUID channelId);

    @Query("select distinct rs from ReadStatus rs join fetch rs.user where rs.channel.id = :channelId")
    List<ReadStatus> findByChannelIdWithUser(@Param("channelId") UUID channelId);
}
