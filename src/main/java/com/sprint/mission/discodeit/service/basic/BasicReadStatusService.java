package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.status.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.status.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.status.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    @Transactional
    public ReadStatus create(CreateReadStatusRequest request) {
        UUID userId = request.userId();
        UUID channelId = request.channelId();

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + userId));

        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다: " + channelId));

        boolean exists = readStatusRepository.findAll().stream()
            .anyMatch(status -> status.getUser().getId().equals(userId)
                && status.getChannel().getId().equals(channelId));

        if (exists) {
            throw new IllegalArgumentException(
                "이미 존재하는 ReadStatus입니다. User ID: " + userId + ", Channel ID: " + channelId);
        }

        ReadStatus readStatus = new ReadStatus(user, channel);
        return readStatusRepository.save(readStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReadStatusResponse> findById(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
            .map(status -> new ReadStatusResponse(
                status.getId(),
                status.getUser().getId(),
                status.getChannel().getId(),
                status.getLastReadAt(),
                status.getCreatedAt(),
                status.getUpdatedAt()
            ));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReadStatusResponse> findAllByUserId(UUID userId) {
        return readStatusRepository.findAll().stream()
            .filter(status -> status.getUser().getId().equals(userId))
            .map(status -> new ReadStatusResponse(
                status.getId(),
                status.getUser().getId(),
                status.getChannel().getId(),
                status.getLastReadAt(),
                status.getCreatedAt(),
                status.getUpdatedAt()
            ))
            .toList();
    }

    @Override
    @Transactional
    public void update(UpdateReadStatusRequest request) {
        UUID readStatusId = request.readStatusId();
        Instant newLastReadAt = request.newLastReadAt();

        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
            .orElseThrow(
                () -> new IllegalArgumentException(
                    "해당 ID의 ReadStatus를 찾을 수 없습니다: " + readStatusId));

        readStatus.updateLastReadAt(newLastReadAt);
//        readStatusRepository.save(readStatus);
    }

    @Override
    @Transactional
    public void deleteById(UUID readStatusId) {
        if (!readStatusRepository.existsById(readStatusId)) {
            throw new IllegalArgumentException("해당 ID의 ReadStatus를 찾을 수 없습니다: " + readStatusId);
        }
        readStatusRepository.deleteById(readStatusId);
    }
}
