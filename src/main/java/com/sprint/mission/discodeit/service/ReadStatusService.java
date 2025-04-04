package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.readStatus.response.ReadStatusFindAllByUserIdResponse;
import com.sprint.mission.discodeit.dto.readStatus.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.readStatus.request.ReadStatusCreatRequest;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    void create(ReadStatusCreatRequest readStatusCreateDto);
    ReadStatusFindAllByUserIdResponse findById(UUID id);
    List<ReadStatusFindAllByUserIdResponse> findAllByUserId(UUID userId);
    void update(ReadStatusUpdateRequest readStatusUpdateDto);
    void delete(UUID id);
}
