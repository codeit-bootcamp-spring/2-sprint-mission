package com.sprint.mission.discodeit.core.message.usecase.status;

import com.sprint.mission.discodeit.adapter.inbound.message.dto.ReadStatusCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.message.dto.ReadStatusUpdateRequestDTO;
import com.sprint.mission.discodeit.core.message.entity.ReadStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ReadStatusService {

  ReadStatus create(UUID userId, UUID channelId,
      ReadStatusCreateRequestDTO readStatusCreateRequestDTO);

  ReadStatus find(UUID readStatusId);

  ReadStatus findByUserId(UUID userId);

  List<ReadStatus> findAllByUserId(UUID userId);

  ReadStatus update(UUID channelId, ReadStatusUpdateRequestDTO requestDTO);

  void delete(UUID readStatusId);
}
