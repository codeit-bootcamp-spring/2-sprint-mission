package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.status.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.status.ReadStatusDto;
import com.sprint.mission.discodeit.dto.status.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private final ReadStatusMapper readStatusMapper;

    @Override
    @Transactional
    public ReadStatusDto create(CreateReadStatusRequest request) {
        UUID userId = request.userId();
        UUID channelId = request.channelId();
        Instant lastReadAt = request.lastReadAt();

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + userId));

        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다: " + channelId));

        Optional<ReadStatus> opt = readStatusRepository.findByUserIdAndChannelId(userId, channelId);
        if (opt.isPresent()) {
            ReadStatus existing = opt.get();
            existing.update(lastReadAt);
            return readStatusMapper.toDto(existing);
        }

        ReadStatus readStatus = new ReadStatus(user, channel, lastReadAt);
        ReadStatus saved = readStatusRepository.save(readStatus);
        return readStatusMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReadStatusDto> findById(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
            .map(readStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findAll().stream()
            .filter(status -> status.getUser().getId().equals(userId))
            .map(readStatusMapper::toDto)
            .toList();
    }

    @Override
    @Transactional
    public ReadStatusDto update(UUID readStatusId, UpdateReadStatusRequest request) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
            .orElseThrow(
                () -> new IllegalArgumentException(
                    "해당 ID의 ReadStatus를 찾을 수 없습니다: " + readStatusId));

        readStatus.update(request.newLastReadAt());
//        readStatusRepository.save(readStatus);
        return null;
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
