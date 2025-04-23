package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.custom.DuplicateResourceException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

        User user = userRepository.findById(userId).orElse(null);
        Channel channel = channelRepository.findById(channelId).orElse(null);

        if (user == null) {
            throw new NoSuchElementException("[ERROR] user not found");
        }
        if (channel == null) {
            throw new NoSuchElementException("[ERROR] channel not found");
        }
        if (readStatusRepository.findAllByUserId(userId).stream()
            .anyMatch(readStatus -> readStatus.getChannel().getId().equals(channelId))) {
            throw new DuplicateResourceException("[ERROR] read status already exist");
        }

        Instant lastReadAt = request.lastReadAt();

        ReadStatus readStatus = ReadStatus.createReadStatus(user, channel, lastReadAt);
        readStatusRepository.save(readStatus);

        return readStatusMapper.toDto(readStatus);
    }

    @Override
    @Transactional
    public ReadStatusDto findById(UUID readStatusId) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId).orElse(null);
        if (readStatus == null) {
            throw new NoSuchElementException("[ERROR] read status not found");
        }

        return readStatusMapper.toDto(readStatus);
    }

    @Override
    @Transactional
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream()
            .map(readStatusMapper::toDto).toList();

    }

    @Override
    @Transactional
    public List<ReadStatusDto> findAllByChannelId(UUID channelId) {
        return readStatusRepository.findAllByChannelId(channelId).stream()
            .map(readStatusMapper::toDto).toList();
    }

    @Override
    @Transactional
    public ReadStatusDto update(UUID readStatusId, UpdateReadStatusRequest request) {
        Instant newLastLeadAt = request.newLastReadAt();

        ReadStatus readStatus = readStatusRepository.findById(readStatusId).orElse(null);
        if (readStatus == null) {
            throw new NoSuchElementException("[ERROR] read status not found");
        }

        readStatus.updateLastReadAt(newLastLeadAt);
        readStatusRepository.save(readStatus);

        return readStatusMapper.toDto(readStatus);
    }

    @Override
    @Transactional
    public void delete(UUID readStatusId) {
        readStatusRepository.deleteById(readStatusId);
        // readStatus가 삭제 되는 시점 -> 유저가 채널에서 퇴장, 채널 삭제
    }
}
