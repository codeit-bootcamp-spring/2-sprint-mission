package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.common.ReadStatus;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
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
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ReadStatus create(ReadStatusCreateRequest requestDto) {
        User user = userRepository.findById(requestDto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("해당 유저 없음"));
        Channel channel = channelRepository.findById(requestDto.channelId())
                .orElseThrow(() -> new ResourceNotFoundException("해당 채널 없음"));

        if (readStatusRepository.findByUserIdAndChannelId(requestDto.userId(), requestDto.channelId()).isPresent()) {
            throw new IllegalArgumentException("해당 유저의 해당 채널 ReadStatus 이미 존재");
        }

        ReadStatus readStatus = new ReadStatus(requestDto.userId(), requestDto.channelId());
        return readStatusRepository.save(readStatus);
    }

    @Override
    public ReadStatus find(UUID id) {
        return readStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 ReadStatus 없음"));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public ReadStatus update(ReadStatusUpdateRequest requestDto) {
        ReadStatus readStatus = readStatusRepository.findById(requestDto.id())
                .orElseThrow(() -> new ResourceNotFoundException("해당 ReadStatus 없음"));

        readStatus.updateLastReadAt();
        return readStatusRepository.save(readStatus);
    }

    @Override
    public void delete(UUID id) {
        if (!readStatusRepository.existsById(id)) {
            throw new ResourceNotFoundException("해당 ReadStatus 없음");
        }
        readStatusRepository.deleteById(id);
    }
}
