package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusReadResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    UUID createReadStatus(ReadStatusCreateRequest readStatusCreateRequest);
    public ReadStatusReadResponse findReadStatusById(UUID readStatusId);
    public ReadStatusReadResponse findReadStatusByUserIdChannelID(UUID userId, UUID channelId);
    List<ReadStatusReadResponse> findAllReadStatusByUserId(UUID userId);
    void updateReadStatus(ReadStatusUpdateRequest readStatusUpdateRequest);
    void deleteReadStatus(UUID readStatusId);
}
