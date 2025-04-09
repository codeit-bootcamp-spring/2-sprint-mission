package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.exception.custom.DuplicateResourceException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
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
            throw new NoSuchElementException("[ERROR] user not found");
        }
        if (channelRepository.findByChannelId(channelId) == null) {
            throw new NoSuchElementException("[ERROR] channel not found");
        }
        if (readStatusRepository.findAllByUserId(userId).stream()
            .anyMatch(readStatus -> readStatus.getChannelId().equals(channelId))) {
            throw new DuplicateResourceException("[ERROR] read status already exist");
        }

        Instant lastReadAt = request.getLastReadAt();

        ReadStatus readStatus = new ReadStatus(channelId, userId, lastReadAt);
        readStatusRepository.save(readStatus);

        return readStatus.getId();
    }

    @Override
    public ReadStatus find(UUID readStatusId) {
        return readStatusRepository.find(readStatusId);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);

    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return readStatusRepository.findAllByChannelId(channelId);
    }

    @Override
    public UUID update(UUID readStatusId, UpdateReadStatusRequest request) {
        Instant newLastLeadAt = request.getNewLastLeadAt();

        ReadStatus readStatus = readStatusRepository.find(readStatusId);
        if (readStatus == null) {
            throw new NoSuchElementException("[ERROR] read status not found");
        }
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
