package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface ReadStatusService {

    ReadStatusDto create(CreateReadStatusRequest request);

    ReadStatusDto findById(UUID readStatusId);

    List<ReadStatusDto> findAllByUserId(UUID userId);

    List<ReadStatusDto> findAllByChannelId(UUID channelId);

    ReadStatusDto update(UUID id, UpdateReadStatusRequest request);
    
    void delete(UUID readStatusId);
}
