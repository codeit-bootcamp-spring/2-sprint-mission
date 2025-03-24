package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatusResponse create(ReadStatusCreateRequest request) {
        if (!userRepository.existsById(request.userId())) {
            throw new NoSuchElementException("User with id " + request.userId() + " not found");
        }
        if (!channelRepository.existsById(request.channelId())) {
            throw new NoSuchElementException("Channel with id " + request.channelId() + " not found");
        }
        if (readStatusRepository.findByUserId(request.userId()).stream()
                .anyMatch(status -> status.getChannelId().equals(request.channelId()))) {
            throw new IllegalArgumentException("ReadStatus already exists for this User and Channel.");
        }

        ReadStatus readStatus = new ReadStatus(request.userId(), request.channelId());
        readStatusRepository.save(readStatus);
        return new ReadStatusResponse(readStatus.getId(), readStatus.getUserId(), readStatus.getChannelId(), readStatus.getUpdatedAt());
    }

    @Override
    public Optional<ReadStatusResponse> find(UUID id) {
        return readStatusRepository.findById(id).map(status ->
                new ReadStatusResponse(status.getId(), status.getUserId(), status.getChannelId(), status.getUpdatedAt()));
    }

    @Override
    public List<ReadStatusResponse> findAllByUserId(UUID userId) {
        return readStatusRepository.findByUserId(userId).stream()
                .map(status -> new ReadStatusResponse(status.getId(), status.getUserId(), status.getChannelId(), status.getUpdatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    public void update(ReadStatusUpdateRequest request) {
        ReadStatus readStatus = readStatusRepository.findById(request.id())
                .orElseThrow(() -> new NoSuchElementException("ReadStatus with id " + request.id() + " not found"));

        readStatus.updateReadStatus();
        readStatusRepository.save(readStatus);
    }

    @Override
    public void delete(UUID id) {
        if (readStatusRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException("ReadStatus with id " + id + " not found");
        }
        readStatusRepository.deleteById(id);
    }
}
