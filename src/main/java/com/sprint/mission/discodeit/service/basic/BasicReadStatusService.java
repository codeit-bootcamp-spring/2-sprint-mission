package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.ReadStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.DTO.ReadStatus.ReadStatusDto;
import com.sprint.mission.discodeit.DTO.ReadStatus.UpdateReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
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
    public ReadStatusDto create(CreateReadStatusDto dto) {
        userRepository.findById(dto.userId())
                .orElseThrow(() -> new NoSuchElementException("User not found: " + dto.userId()));
        channelRepository.findById(dto.channelId())
                .orElseThrow(() -> new NoSuchElementException("Channel not found: " + dto.channelId()));

        if (readStatusRepository.findByUserIdAndChannelId(dto.userId(), dto.channelId()).isPresent()) {
            throw new IllegalStateException("A ReadStatus for this user and channel already exists");
        }

        ReadStatus readStatus = new ReadStatus(dto.userId(), dto.channelId());
        return mapToDto(readStatusRepository.save(readStatus));
    }

    @Override
    public ReadStatusDto find(UUID id) {
        return readStatusRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new NoSuchElementException("ReadStatus not found: " + id));
    }

    @Override
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public ReadStatusDto update(UpdateReadStatusDto dto) {
        ReadStatus existing = readStatusRepository.findById(dto.id())
                .orElseThrow(() -> new NoSuchElementException("ReadStatus not found: " + dto.id()));

        if (dto.lastReadAt() != null) {
            existing.updateLastReadAt(dto.lastReadAt());
        }

        return mapToDto(readStatusRepository.save(existing));
    }

    @Override
    public void delete(UUID id) {
        readStatusRepository.deleteById(id);
    }

    private ReadStatusDto mapToDto(ReadStatus readStatus) {
        return new ReadStatusDto(
                readStatus.getId(),
                readStatus.getUserId(),
                readStatus.getChannelId(),
                readStatus.getCreatedAt(),
                readStatus.getUpdatedAt(),
                readStatus.getLastReadAt()
        );
    }
}

