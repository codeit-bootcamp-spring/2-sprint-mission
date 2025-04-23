package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {
    List<ReadStatus> findAllByUser_Id(UUID userId);
    List<ReadStatus> findAllByChannel_Id(UUID channelId);
    List<ReadStatus> findAllByUser(User user);
    List<ReadStatus> findAllByChannel(Channel channel);
    void deleteAllByChannel(Channel channel);
}
