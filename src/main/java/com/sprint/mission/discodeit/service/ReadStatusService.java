package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreate;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdate;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatusCreate dto);
    ReadStatus findById(UUID id);
    List<ReadStatus> findAllByUserId(UUID userId);
    ReadStatus update(ReadStatusUpdate dto);
    void delete(UUID id);
}
