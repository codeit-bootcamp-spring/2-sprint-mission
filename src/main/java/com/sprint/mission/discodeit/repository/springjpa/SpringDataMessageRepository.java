package com.sprint.mission.discodeit.repository.springjpa;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.UUID;

@Component
public interface SpringDataMessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findAllByChannel_Id(UUID channelId);
    Page<Message> findAllByChannel_IdOrderByCreatedAtDesc(UUID channelId, Pageable pageable);
    void deleteAllByChannel_Id(UUID channelId);
}
