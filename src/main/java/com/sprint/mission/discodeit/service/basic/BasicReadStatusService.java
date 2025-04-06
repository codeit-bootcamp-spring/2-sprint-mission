package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatus create(ReadStatusCreateRequest request) {
        UUID userId = request.userId();
        UUID channelId = request.channelId();

        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("사용자를 찾을 수 없습니다.");
        }
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("채널을 찾을 수 없습니다.");
        }
        if (readStatusRepository.findAllByUserId(userId).stream()
                .anyMatch(readStatus -> readStatus.getChannelId().equals(channelId))) {
            throw new IllegalArgumentException("ReadStatus with userId " + userId + " and channelId " + channelId + " already exists");
        }

        Instant readAt = request.lastReadAt();
        ReadStatus readStatus = new ReadStatus(userId, channelId, readAt);
        return readStatusRepository.save(readStatus);
    }

    @Override
    public ReadStatus findById(UUID readStatusId) {
        return getReadStatus(readStatusId);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream()
                .toList();
    }

    @Override
    public ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest dto) {
        Instant newReadAt = dto.newLastReadAt();
        ReadStatus readStatus = getReadStatus(readStatusId);
        readStatus.updateReadAt(newReadAt);

        return readStatusRepository.save(readStatus);
    }

    @Override
    public ReadStatus updateByChannelIdAndUserId(UUID userId, UUID channelId, ReadStatusUpdateRequest dto) {
        Instant newReadAt = dto.newLastReadAt();
        ReadStatus readStatus = readStatusRepository.findAllByUserId(userId).stream()
                .filter(status -> status.getChannelId().equals(channelId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ReadStatus not found for the given userId and channelId"));

        readStatus.updateReadAt(newReadAt);
        return readStatusRepository.save(readStatus);
    }

    @Override
    public void delete(UUID readStatusId) {
        if (!readStatusRepository.existsById(readStatusId)) {
            throw new NoSuchElementException("ReadStatus with id " + readStatusId + " not found");
        }
        readStatusRepository.deleteById(readStatusId);
    }

    private ReadStatus getReadStatus(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new NoSuchElementException("ReadStatus with id " + readStatusId + " not found"));
    }
}