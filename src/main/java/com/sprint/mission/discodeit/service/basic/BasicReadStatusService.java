package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusDTO;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusDTO;
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
    public ReadStatus create(CreateReadStatusDTO dto) {
        UUID userId = dto.userId();
        UUID channelId = dto.channelId();

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

        Instant readAt = dto.readAt();
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
    public ReadStatus update(UUID readStatusId, UpdateReadStatusDTO dto) {
        Instant newReadAt = dto.readAt();
        ReadStatus readStatus = getReadStatus(readStatusId);
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
                .orElseThrow(()->new NoSuchElementException("ReadStatus with id " + readStatusId + " not found"));
    }
}