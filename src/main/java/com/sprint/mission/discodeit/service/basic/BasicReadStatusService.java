package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusMapper readStatusMapper;


    @PreAuthorize("authentication.principal.user.id == #request.userId")
    @Transactional
    @Override
    public ReadStatusDto create(ReadStatusCreateRequest request) {
        UUID userId = request.userId();
        UUID channelId = request.channelId();

        User user = findUserOrThrow(userId);
        Channel channel = findChannelOrThrow(channelId);

        if (readStatusRepository.existsByUserIdAndChannelId(userId, channelId)) {
            throw new IllegalStateException("이미 존재하는 읽음 상태입니다.");
        }

        ReadStatus readStatus = new ReadStatus(user, channel, request.lastReadAt());
        ReadStatus savedReadStatus = readStatusRepository.save(readStatus);

        return readStatusMapper.toDto(savedReadStatus);
    }

    @Transactional(readOnly = true)
    @Override
    public ReadStatusDto find(UUID readStatusId) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
            .orElseThrow(() -> new ReadStatusNotFoundException(readStatusId));

        return readStatusMapper.toDto(readStatus);
    }


    @Transactional(readOnly = true)
    @Override
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        List<ReadStatus> readStatuses = readStatusRepository.findAllByUserId(userId);
        log.info("읽기 상태 목록 조회 완료");

        return readStatuses.stream().map(readStatusMapper::toDto).collect(Collectors.toList());
    }


    @PreAuthorize("@readStatusRepository.findById(#readStatusId).orElse(null)?.user?.id == authentication.principal.user.id")
    @Transactional
    @Override
    public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
            .orElseThrow(() -> new ReadStatusNotFoundException(readStatusId));

        Instant newLastReadAt = request.newLastReadAt();
        if (newLastReadAt != null) {
            readStatus.update(newLastReadAt);
        }

        ReadStatus updatedReadStatus = readStatusRepository.save(readStatus);
        log.info("읽기 상태 수정 완료");

        return readStatusMapper.toDto(updatedReadStatus);
    }

    private User findUserOrThrow(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    private Channel findChannelOrThrow(UUID channelId) {
        return channelRepository.findById(channelId)
            .orElseThrow(() -> new ChannelNotFoundException(channelId));
    }

}
