package com.sprint.mission.discodeit.core.message.repository;

import com.sprint.mission.discodeit.core.message.entity.Message;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMessageRepository extends JpaRepository<Message, UUID>,
    CustomMessageRepository {

}
