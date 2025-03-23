package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.CreateReadStatusDTO;
import com.sprint.mission.discodeit.dto.UpdateReadStatusDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.LifecycleState;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatus create(CreateReadStatusDTO readStatusDTO) {
        Channel channel = channelRepository.findById(readStatusDTO.getChannelId())
                .orElseThrow(() -> new NoSuchElementException("Channel not found"));
        User user = userRepository.findById(readStatusDTO.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        ReadStatus existing = readStatusRepository.findByUserAndChannel(readStatusDTO.getUserId(), channel.getId());
        if (existing != null) {
            throw new IllegalStateException("ReadStatus already exists");
        }
        Instant lastRead = readStatusDTO.getLastReadAt() != null ? readStatusDTO.getLastReadAt() : Instant.now();

        ReadStatus readStatus = new ReadStatus(
                UUID.randomUUID(),
                readStatusDTO.getUserId(),
                readStatusDTO.getChannelId(),
                lastRead
        );
        readStatusRepository.save(readStatus);
        return readStatus;
    }

    @Override
    public ReadStatus find(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id);
        return Optional.ofNullable(readStatus)
                .orElseThrow(()-> new NoSuchElementException("ReadStatus not found"));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId){
        return readStatusRepository.findAll().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public ReadStatus update(UpdateReadStatusDTO updateReadStatusDTO){
        ReadStatus readStatus = readStatusRepository.findById(updateReadStatusDTO.getId());
        Optional.ofNullable(readStatus).orElseThrow(()-> new NoSuchElementException("ReadStatus not found"));
        readStatus.setLastRead(updateReadStatusDTO.getLastReadAt());
        readStatusRepository.save(readStatus);
        return readStatus;
    }

    @Override
    public void delete(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id);
        Optional.ofNullable(readStatus).orElseThrow(()-> new NoSuchElementException("ReadStatus not found"));
        readStatusRepository.delete(readStatus);
    }
}
