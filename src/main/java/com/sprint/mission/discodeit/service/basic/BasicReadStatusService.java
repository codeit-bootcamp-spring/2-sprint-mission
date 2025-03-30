package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.application.dto.readstatus.ReadStatusResult;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @Override
    public ReadStatusResult create(UUID userId, UUID channelId) {
        channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("readStaus에 해당하는 채널이 없습니다."));
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("readStatus에 해당하는 유저가 없습니다."));

        List<ReadStatus> readStatuses = readStatusRepository.findByChannelId(channelId);
        boolean isExisting = readStatuses.stream()
                .anyMatch(readStatus -> readStatus.getUserId().equals(userId));
        if (isExisting) {
            throw new IllegalArgumentException("채널과 유저 사이의 ReadStatus는 하나만 있을 수 있습니다.");
        }

        ReadStatus readStatus = readStatusRepository.save(new ReadStatus(userId, channelId));

        return ReadStatusResult.fromEntity(readStatus);
    }

    @Override
    public List<ReadStatusResult> findByChannelId(UUID channelId) {
        List<ReadStatus> readStatuses = readStatusRepository.findByChannelId(channelId);
        return ReadStatusResult.fromEntity(readStatuses);
    }

    @Override
    public ReadStatusResult findByReadStatusId(UUID readStatusId) {
        ReadStatus readStatus = readStatusRepository.find(readStatusId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Id의 객체가 없습니다."));

        return ReadStatusResult.fromEntity(readStatus);
    }

    @Override
    public List<ReadStatusResult> findAllByUserId(UUID useId) {
        List<ReadStatus> readStatuses = readStatusRepository.findByUserId(useId);
        return ReadStatusResult.fromEntity(readStatuses);
    }

    @Override
    public ReadStatusResult updateLastReadTime(UUID readStatusId) {
        ReadStatus readStatus = readStatusRepository.find(readStatusId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Id의 객체가 없습니다."));

        readStatus.updateLastReadTime();
        ReadStatus savedReadStatus = readStatusRepository.save(readStatus);

        return ReadStatusResult.fromEntity(savedReadStatus);
    }

    @Override
    public void delete(UUID readStatusId) {
        readStatusRepository.delete(readStatusId);
    }
}
