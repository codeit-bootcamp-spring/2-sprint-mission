package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public ReadStatusDto create(ReadStatusCreateRequest request) {
        UUID userId = request.userId();
        UUID channelId = request.channelId();

        User user = userRepository.findById(userId).orElseThrow(() ->
                new NoSuchElementException("사용자를 찾을 수 없습니다."));

        Channel channel = channelRepository.findById(channelId).orElseThrow(() ->
                new NoSuchElementException("채널을 찾을 수 없습니다."));

        if (readStatusRepository.findAllByUser_Id(userId).stream()
                .anyMatch(readStatus -> readStatus.getChannel().getId().equals(channelId))) {
            throw new IllegalArgumentException("ReadStatus with userId " + userId + " and channelId " + channelId + " already exists");
        }

        Instant readAt = request.lastReadAt();
        ReadStatus readStatus = new ReadStatus(user, channel, readAt);
        readStatusRepository.save(readStatus);
        return toDto(readStatus);
    }

    @Transactional(readOnly = true)
    @Override
    public ReadStatus findById(UUID readStatusId) {
        return getReadStatus(readStatusId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUser_Id(userId).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    @Override
    public ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest dto) {
        Instant newReadAt = dto.newLastReadAt();
        ReadStatus readStatus = getReadStatus(readStatusId);
        readStatus.updateReadAt(newReadAt);

        return readStatus;
    }

    @Transactional
    @Override
    public ReadStatus updateByChannelIdAndUserId(UUID userId, UUID channelId, ReadStatusUpdateRequest dto) {
        Instant newReadAt = dto.newLastReadAt();
        ReadStatus readStatus = readStatusRepository.findAllByUser_Id(userId).stream()
                .filter(status -> status.getChannel().getId().equals(channelId))
                .findFirst()
                .orElseThrow(() ->
                        new NoSuchElementException("ReadStatus not found for the given userId and channelId"));

        readStatus.updateReadAt(newReadAt);
        return readStatus;
    }

    @Transactional
    @Override
    public void delete(UUID readStatusId) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new NoSuchElementException("ReadStatus with id " + readStatusId + " not found"));

        readStatusRepository.delete(readStatus);
    }

    private ReadStatus getReadStatus(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new NoSuchElementException("ReadStatus with id " + readStatusId + " not found"));
    }

    private ReadStatusDto toDto(ReadStatus readStatus) {
        return new ReadStatusDto(
                readStatus.getId(),
                readStatus.getUser().getId(),
                readStatus.getChannel().getId(),
                readStatus.getLastReadAt()
        );
    }
}