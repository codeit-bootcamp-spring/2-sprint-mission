package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
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
    public ReadStatus create(ReadStatusDto readStatusDto) {
        if (!userRepository.existsById(readStatusDto.userId())) {
            throw new NoSuchElementException("해당 사용자를 찾을 수 없습니다.");
        }
        if (!channelRepository.existsById(readStatusDto.channelId())) {
            throw new NoSuchElementException("해당 채널를 찾을 수 없습니다.");
        }
        if (readStatusRepository.existsByUserIdAndChannelId(readStatusDto.userId(), readStatusDto.channelId())) {
            throw new IllegalStateException("해당 사용자의 상태 정보가 이미 등록되어 있습니다.");
        }

        Instant lastReadAt = readStatusDto.lastReadAt();
        ReadStatus readStatus = new ReadStatus(readStatusDto.userId(), readStatusDto.channelId(), lastReadAt);

        return readStatusRepository.save(readStatus);
    }

    @Override
    public ReadStatus findById(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new NoSuchElementException("읽음 상태 정보를 찾을 수 없습니다."));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream().toList();
    }

    @Override
    public ReadStatus update(UUID readStatusId, ReadStatusDto readStatusDto) {
        ReadStatus readStatusExists = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new NoSuchElementException("읽음 상태 정보를 찾을 수 없습니다."));

        Instant lastReadAt = readStatusDto.lastReadAt();

        readStatusExists.update(lastReadAt);

        return readStatusRepository.save(readStatusExists);
    }

    @Override
    public void delete(UUID readStatusId) {
        if (!readStatusRepository.existsById(readStatusId)) {
            throw new NoSuchElementException("읽음 상태 정보를 찾을 수 없습니다.");
        }

        readStatusRepository.deleteById(readStatusId);
    }
}
