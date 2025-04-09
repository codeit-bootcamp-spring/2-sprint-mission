package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface ReadStatusService {

    UUID create(CreateReadStatusRequest request);

    ReadStatus find(UUID readStatusId);

    List<ReadStatus> findAllByUserId(UUID userId);

    List<ReadStatus> findAllByChannelId(UUID channelId);

    UUID update(UUID id, UpdateReadStatusRequest request);

    void updateByChannelId(UUID channelId, UpdateReadStatusRequest request);

    void delete(UUID readStatusId);
}
