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
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicReadStatusServiceImp implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    // 수신 정보 등록, 새로운 메세지 읽음 여부
    @Override
    public ReadStatus create(ReadStatusCreateRequest request) {
        UUID userId = request.userId();
        UUID channelId = request.channelId();

        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User with id " + userId + " does not exist");
        }
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " does not exist");
        }
        // 유저가 이미 읽은 상태 (톡방을 실시간으로 보고 있는 경우, 최신 메세지를 본 경우)에 대한 중복 읽음 상태 등록 방지
        if (readStatusRepository.findAllByUserId(userId).stream()
                .anyMatch(readStatus -> readStatus.getChannelId().equals(channelId))) {
            throw new IllegalArgumentException("ReadStatus with userId " + userId + " and channelId " + channelId + " already exists");
        }

        Instant lastReadAt = request.lastReadAt();
        ReadStatus readStatus = new ReadStatus(userId, channelId, lastReadAt);
        return readStatusRepository.save(readStatus);
    }

    @Override
    public ReadStatus find(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new NoSuchElementException("ReadStatus with id " + readStatusId + " not found"));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream()
                .toList();
    }

    @Override
    public ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest request) {
        Instant newLastReadAt = request.newLastReadAt();
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new NoSuchElementException("ReadStatus with id " + readStatusId + " not found"));
        readStatus.update(newLastReadAt);
        return readStatusRepository.save(readStatus);
    }

    @Override
    public void delete(UUID readStatusId) {
        if (!readStatusRepository.existsById(readStatusId)) {
            throw new NoSuchElementException("ReadStatus with id " + readStatusId + " not found");
        }
        readStatusRepository.deleteById(readStatusId);
    }
}
