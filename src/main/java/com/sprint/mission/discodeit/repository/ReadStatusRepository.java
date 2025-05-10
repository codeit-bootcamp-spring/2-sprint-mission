package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

    List<ReadStatus> findAllByUserId(UUID userId);

    boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);

    @Modifying
    @Transactional
    void deleteAllByChannelId(UUID channelId);

    List<ReadStatus> findAllByChannelId(UUID channelId);
}
