package com.sprint.mission.discodeit.repository.springjpa;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public interface SpringDataReadStatusRepository extends JpaRepository<ReadStatus, UUID> {
    List<ReadStatus> findAllByUser_Id(UUID userId);
    List<ReadStatus> findAllByChannel_Id(UUID channelId);
    void deleteAllByChannel_Id(UUID channelId);

}
