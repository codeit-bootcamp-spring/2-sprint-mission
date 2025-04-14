package com.sprint.mission.discodeit.adapter.outbound.message;

import com.sprint.mission.discodeit.core.message.entity.Message;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMessageRepository extends JpaRepository<Message, UUID> {


  List<Message> findAllByChannel_Id(UUID channelId);
}
