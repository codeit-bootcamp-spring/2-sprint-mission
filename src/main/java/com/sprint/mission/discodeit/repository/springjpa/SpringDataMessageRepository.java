package com.sprint.mission.discodeit.repository.springjpa;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public interface SpringDataMessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findAllByChannel_Id(UUID channelId);
    void deleteAllByChannel_Id(UUID channelId);
}
