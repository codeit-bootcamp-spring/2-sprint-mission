package com.sprint.mission.discodeit.service.domain;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.dto.readStatus.request.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.readStatus.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.readStatus.response.ReadStatusResponse;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public ReadStatusResponse create(ReadStatusRequest request){
        userRepository.findById(request.userId())
                .orElseThrow(() -> new NoSuchElementException("User with id " + request.userId() + " not found"));
        channelRepository.findById(request.channelId())
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + request.channelId() + " not found"));

        boolean exists = readStatusRepository.findAll().stream()
                .anyMatch(rs ->
                        rs.getId().equals(request.userId()) &&
                                rs.getChannelId().equals(request.channelId())
                );

        if(exists){
            throw new IllegalArgumentException("ReadStatus related to the same Channel and User already exists");
        }

        ReadStatus readStatus = new ReadStatus(request.userId(), request.channelId(), request.lastReadTime());
        readStatusRepository.save(readStatus);

        return new ReadStatusResponse(readStatus.getId(), readStatus.getLastReadTime());
    }

    public ReadStatus find(UUID id){
        return readStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("ReadStatus with id " + id + " not found"));
    }

    public List<ReadStatus> findAllByUserId(UUID userId){
        return readStatusRepository.findAll().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }

    public ReadStatusResponse update(ReadStatusUpdateRequest request){
        ReadStatus readStatus = readStatusRepository.findById(request.id())
                .orElseThrow(() -> new NoSuchElementException("ReadStatus with id " + request.id() + " not found"));

        readStatus.updateLastReadTime(request.lastReadTime());
        readStatusRepository.save(readStatus);

        return new ReadStatusResponse(readStatus.getId(), readStatus.getLastReadTime());
    }

    public void delete(UUID id){
        readStatusRepository.delete(id);
    }
}
