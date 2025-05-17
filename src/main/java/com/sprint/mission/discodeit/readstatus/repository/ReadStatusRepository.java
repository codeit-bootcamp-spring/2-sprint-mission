package com.sprint.mission.discodeit.readstatus.repository;

import com.sprint.mission.discodeit.readstatus.entity.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {
    @Query("select r from ReadStatus r where r.channel.id = :channelId and r.user.id = :userId")
    Optional<ReadStatus> findByChannelIdAndUserId(@Param("channelId") UUID channelId, @Param("userId") UUID userId);

    List<ReadStatus> findByChannel_Id(UUID channelId);

    List<ReadStatus> findByUser_Id(UUID userId);

    void deleteAllByChannel_Id(UUID channelId);
}
