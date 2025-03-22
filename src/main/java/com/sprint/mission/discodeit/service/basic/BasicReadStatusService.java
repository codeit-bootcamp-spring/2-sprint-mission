package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.application.ReadStatus.ReadStatusesDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ReadStatusesDto findByChannelId(UUID channelId) {
        List<ReadStatus> readStatuses = readStatusRepository.findByChannelId(channelId);
        return ReadStatusesDto.fromEntity(readStatuses);
    }
}
