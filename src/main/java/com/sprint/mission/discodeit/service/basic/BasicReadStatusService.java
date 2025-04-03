package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exceptions.InvalidInputException;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.readstatusdto.ReadStatusCreateDto;
import com.sprint.mission.discodeit.service.dto.readstatusdto.ReadStatusDeleteDto;
import com.sprint.mission.discodeit.service.dto.readstatusdto.ReadStatusFindDto;
import com.sprint.mission.discodeit.service.dto.readstatusdto.ReadStatusUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatus create(ReadStatusCreateDto readStatusCreateDto) {
        List<User> userList = userRepository.load();
        List<Channel> channelList = channelRepository.load();
        List<ReadStatus> readStatusList = readStatusRepository.load();

        User matchingUser = userList.stream()
                .filter(m -> m.getId().equals(readStatusCreateDto.userId()))
                .findAny()
                .orElseThrow(() -> new NotFoundException("User not found"));
        Channel matchingChannel = channelList.stream()
                .filter(m -> m.getId().equals(readStatusCreateDto.channelId()))
                .findAny()
                .orElseThrow(() -> new NotFoundException("Channel not found"));
        Optional<ReadStatus> matchingReadStatus = readStatusList.stream()
                .filter(m -> m.getUserId().equals(readStatusCreateDto.userId()) && m.getChannelId().equals(readStatusCreateDto.channelId()))
                .findAny();
        if (matchingReadStatus.isPresent()) {
            throw new InvalidInputException("Read status already exists");
        }

        Instant lastReadAt = readStatusCreateDto.lastReadTime();
//        Instant lastReadAt = Instant.now();
        ReadStatus readStatus = new ReadStatus(matchingUser.getId(), matchingChannel.getId(), lastReadAt);
        readStatusRepository.save(readStatus);
        return readStatus;

    }


    @Override
    public ReadStatus find(ReadStatusFindDto readStatusFindDto) {
        return readStatusRepository.load().stream()
                .filter(r -> r.getId().equals(readStatusFindDto.Id()))
                .findAny()
                .orElseThrow(() -> new NotFoundException("readStatus not found"));
    }


    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.load().stream()
                .filter(m -> m.getUserId().equals(userId))
                .toList();
    }


    @Override
    public ReadStatus update(UUID readStatusId, ReadStatusUpdateDto readStatusUpdateDto) {
        ReadStatus matchingReadStatus = readStatusRepository.load().stream()
                .filter(m -> m.getId().equals(readStatusId))
                .findAny()
                .orElseThrow(() -> new NotFoundException("readStatus not found"));
        Instant newLastReadTime = readStatusUpdateDto.newLastReadTime();
//        Instant newLastReadTime = Instant.now();
        matchingReadStatus.readStatusUpdate(newLastReadTime);
        return readStatusRepository.save(matchingReadStatus);
    }


    @Override
    public void delete(ReadStatusDeleteDto readStatusDeleteDto) {
        ReadStatus matchingReadStatus = readStatusRepository.load().stream()
                .filter(m -> m.getId().equals(readStatusDeleteDto.Id()))
                .findAny()
                .orElseThrow(() -> new NotFoundException("readStatus not found"));
        readStatusRepository.remove(matchingReadStatus);
    }
}
