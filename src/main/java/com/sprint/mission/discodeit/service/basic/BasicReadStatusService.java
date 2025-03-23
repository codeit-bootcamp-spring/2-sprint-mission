package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatus create(ReadStatusCreateRequestDto requestDto) {
        UUID userId = requestDto.getUserId();
        UUID channelId = requestDto.getChannelId();

        if(!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User with id" + userId + "not found");
        }

        if(!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id" + channelId + "not found");
        }

        if (readStatusRepository.findByUserAndChannel(userId, channelId).isPresent()){
            throw new IllegalArgumentException("ReadStatus already exists for user" + userId + "and channel" + channelId);
        }

        ReadStatus readStatus = new ReadStatus(userId, channelId, Instant.now());
        readStatusRepository.save(readStatus);
        return readStatus;
    }
}
