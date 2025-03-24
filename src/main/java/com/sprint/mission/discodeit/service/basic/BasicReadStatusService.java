package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.ReadStatusDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Primary
@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatus createReadStatus(ReadStatusDTO readStatusDTO) {
        User user = userRepository.findById(readStatusDTO.userId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 User ID입니다: " + readStatusDTO.userId()));

        Channel channel = channelRepository.findById(readStatusDTO.channelId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Channel ID입니다: " + readStatusDTO.channelId()));

        ReadStatus readStatus = new ReadStatus(
                UUID.randomUUID(),
                Instant.now(),
                Instant.now(),
                user,
                channel,
                readStatusDTO.lastReadTime().toInstant(ZoneOffset.UTC)
        );
        readStatusRepository.save(readStatus);
        return readStatus;
    }

    @Override
    public ReadStatus getReadStatus(UUID userId, UUID channelId) {
        return readStatusRepository.findById(userId, channelId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ReadStatus입니다: userId=" + userId + ", channelId=" + channelId));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public void updateReadStatus(UUID userId, UUID channelId, ReadStatusDTO readStatusDTO) {
        ReadStatus readStatus = getReadStatus(userId, channelId);

        ReadStatus updatedReadStatus = new ReadStatus(
                readStatus.getId(),
                readStatus.getCreatedAt(),
                Instant.now(),
                readStatus.getUser(),
                readStatus.getChannel(),
                readStatusDTO.lastReadTime().toInstant(ZoneOffset.UTC)
        );

        readStatusRepository.save(updatedReadStatus);
    }

    @Override
    public void deleteReadStatus(UUID userId, UUID channelId) {
        getReadStatus(userId, channelId); // 존재 여부 확인
        readStatusRepository.delete(userId, channelId);
    }
}