package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public UUID create(CreateReadStatusRequest request) {
        UUID userId = request.getUserId();
        UUID channelId = request.getChannelId();

        if (userRepository.findByUserId(userId) == null) {
            throw new IllegalArgumentException("[ERROR] user not found");
        }
        if (channelRepository.findByChannelId(channelId) == null) {
            throw new IllegalArgumentException("[ERROR] channel not found");
        }
        if (readStatusRepository.isExist(request.getChannelId(),
            request.getUserId()) != null) {
            throw new IllegalArgumentException("[ERROR] read status is already exist");
        }

        Instant lastReadAt = request.getLastReadAt();

        ReadStatus readStatus = new ReadStatus(channelId, userId, lastReadAt);
        readStatusRepository.save(readStatus);

        return readStatus.getId();
    }

    @Override
    public ReadStatusResponseDto find(UUID readStatusId) {
        ReadStatus readStatus = readStatusRepository.find(readStatusId);
        return ReadStatusResponseDto.from(readStatus);
    }

    @Override
    public List<ReadStatusResponseDto> findAllByUserId(UUID userId) {
        List<ReadStatus> readStatuses = readStatusRepository.
            findAllByUserId(userId);

        return readStatuses.stream()
            .map(ReadStatusResponseDto::from).toList();
    }

    @Override
    public List<ReadStatusResponseDto> findAllByChannelId(UUID channelId) {
        List<ReadStatus> readStatuses = readStatusRepository.
            findAllByChannelId(channelId);

        return readStatuses.stream()
            .map(ReadStatusResponseDto::from).toList();
    }

    @Override
    public UUID update(UUID readStatusId, UpdateReadStatusRequest request) {
        Instant newLastLeadAt = request.getNewLastLeadAt();

        ReadStatus readStatus = readStatusRepository.find(readStatusId);
        readStatus.updateLastReadAt(newLastLeadAt);

        readStatusRepository.save(readStatus);

        return readStatus.getId();
    }

    @Override
    public void updateByChannelId(UUID channelId, UpdateReadStatusRequest request) {
        Instant newLastLeadAt = request.getNewLastLeadAt();

        List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelId(channelId);

        for (ReadStatus readStatus : readStatuses) {
            readStatus.updateLastReadAt(newLastLeadAt);
        }
    }

    @Override
    public void delete(UUID readStatusId) {
        readStatusRepository.delete(readStatusId);
    }
}
