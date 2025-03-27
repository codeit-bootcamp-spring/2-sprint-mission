package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatus create(ReadStatusCreateRequest request) {
        if (userRepository.findByKey(request.userKey()) == null) {
            throw new IllegalArgumentException("[Error] user is null");
        }
        if (channelRepository.findByKey(request.channelKey()) == null) {
            throw new IllegalArgumentException("[Error] channel is null");
        }
        ReadStatus readStatus = new ReadStatus(request.userKey(), request.channelKey(), Instant.now());
        readStatusRepository.save(readStatus);

        return readStatus;
    }

    @Override
    public ReadStatus find(UUID readStatusKey) {
        ReadStatus readStatus = readStatusRepository.findByKey(readStatusKey);
        if (readStatus == null) {
            throw new IllegalArgumentException("[Error] ReadStatus null");
        }

        return readStatus;
    }

    @Override
    public List<ReadStatus> findAllByUserKey(UUID userKey) {
        List<ReadStatus> readStatuses = readStatusRepository.findAllByUserKey(userKey);
        if (readStatuses == null || readStatuses.isEmpty()) {
            throw new IllegalArgumentException("[Error] ReadStatus null");
        }

        return readStatuses;
    }

    @Override
    public ReadStatus update(ReadStatusUpdateRequest request) {
        ReadStatus readStatus = readStatusRepository.findByKey(request.readStatusKey());
        if (readStatus == null) {
            throw new IllegalArgumentException("[Error] readStatus is null");
        }
        readStatus.updateLastReadAt(Instant.now());
        readStatusRepository.save(readStatus);

        return readStatus;
    }

    @Override
    public void delete(UUID readStatusKey) {
        ReadStatus readStatus = readStatusRepository.findByKey(readStatusKey);
        if (readStatus == null) {
            throw new IllegalArgumentException("[Error] ReadStatus null");
        }
        readStatusRepository.delete(readStatusKey);
    }
}
