package com.sprint.mission.discodeit.adapter.outbound.message;

import com.sprint.mission.discodeit.core.message.entity.Message;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMessageRepository extends JpaRepository<Message, UUID> {

  Slice<Message> findById(UUID id, Pageable pageable);

  List<Message> findAllByChannel_Id(UUID channelId);

  Slice<Message> findAllByChannel_Id(UUID channelId, Pageable pageable);
}
