package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusDTO;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        Channel channel = channelRepository.findById(dto.channelId())
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다."));

        if (readStatusRepository.findByUserIdAndChannelId(dto.userId(), dto.channelId()).isPresent()) {
            throw new IllegalArgumentException("ReadStatus가 이미 존재합니다.");
        }

        ReadStatus readStatus = new ReadStatus(dto.userId(), dto.channelId(), dto.readAt());
        return readStatusRepository.save(readStatus);
    }

    @Override
    public ReadStatus findById(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new NoSuchElementException("읽은 상태를 찾을 수 없습니다."));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public ReadStatus update(UpdateReadStatusDTO dto) {
        ReadStatus readStatus = readStatusRepository.findById(dto.readStatusId())
                .orElseThrow(() -> new NoSuchElementException("ReadStatus를 찾을 수 없습니다."));
        readStatus.updateReadAt(dto.readAt());
    }

    @Override
    public void delete(UUID readStatusId) {
        if (!readStatusRepository.existsById(readStatusId)) {
            throw new NoSuchElementException("ReadStatus를 찾을 수 없습니다.");
        }
        readStatusRepository.deleteById(readStatusId);
    }
}