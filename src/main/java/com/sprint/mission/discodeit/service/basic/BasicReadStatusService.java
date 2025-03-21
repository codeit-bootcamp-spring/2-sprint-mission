package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    ReadStatusRepository readStatusRepository;
    UserRepository userRepository;
    ChannelRepository channelRepository;

    @Override
    public ReadStatus create(CreateReadStatusRequest request) {
        UUID userId = request.userId();
        UUID channelId = request.channelId();

        if (userRepository.getUserById(userId).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 ID입니다: " + userId);
        }

        if (channelRepository.getChannelById(channelId).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 채널 ID입니다: " + channelId);
        }

        Optional<ReadStatus> existingReadStatus = readStatusRepository.getAll().stream()
                .filter(status -> status.getUserId().equals(userId)
                        && status.getChannelId().equals(channelId))
                .findFirst();

        if (existingReadStatus.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 ReadStatus입니다. User ID: " + userId + ", Channel ID: " + channelId);
        }

        ReadStatus readStatus = new ReadStatus(userId, channelId);
        readStatusRepository.save(readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID readStatusId) {
        return readStatusRepository.getById(readStatusId);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.getAll().stream()
                .filter(status -> status.getUserId().equals(userId))
                .toList();
    }

    @Override
    public void update(UpdateReadStatusRequest request) {
        UUID readStatusId = request.readStatusId();
        Instant newLastReadAt = request.lastReadAt();

        ReadStatus readStatus = readStatusRepository.getById(readStatusId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 ReadStatus를 찾을 수 없습니다: " + readStatusId));

        readStatus.updateLastReadAt(newLastReadAt);
        readStatusRepository.save(readStatus);
    }

    @Override
    public void deleteById(UUID readStatusId) {
        ReadStatus readStatus = readStatusRepository.getById(readStatusId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 ReadStatus를 찾을 수 없습니다: " + readStatusId));

        readStatusRepository.deleteById(readStatusId);
    }
}
