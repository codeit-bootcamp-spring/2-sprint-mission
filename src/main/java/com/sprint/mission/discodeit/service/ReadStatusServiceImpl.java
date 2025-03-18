package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequestDTO;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequestDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReadStatusServiceImpl implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public ReadStatusServiceImpl(ReadStatusRepository readStatusRepository,
                                 UserRepository userRepository,
                                 ChannelRepository channelRepository) {
        this.readStatusRepository = readStatusRepository;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public ReadStatus create(ReadStatusCreateRequestDTO dto) {
        if (!userRepository.existsById(dto.getUserId())) {
            throw new NoSuchElementException("User not found with id " + dto.getUserId());
        }

        if (!channelRepository.existsById(dto.getChannelId())) {
            throw new NoSuchElementException("Channel not found with id " + dto.getChannelId());
        }

        if (readStatusRepository.existsByUserIdAndChannelId(dto.getUserId(), dto.getChannelId())) {
            throw new IllegalArgumentException("ReadStatus already exists for this user and channel.");
        }

        ReadStatus readStatus = new ReadStatus(UUID.randomUUID(), dto.getUserId(), false);
        return readStatusRepository.save(readStatus);
    }

    @Override
    public Optional<ReadStatus> find(UUID userId, UUID channelId) {
        return readStatusRepository.findById(userId, channelId);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public ReadStatus update(ReadStatusUpdateRequestDTO dto) {
        ReadStatus readStatus = readStatusRepository.findById(dto.getUserId(), dto.getChannelId())
                .orElseThrow(() -> new NoSuchElementException("ReadStatus not found for user "
                        + dto.getUserId() + " and channel "
                        + dto.getChannelId()));

        readStatus.setRead(dto.isRead());  // 상태 업데이트
        return readStatusRepository.save(readStatus);
    }

    @Override
    public void delete(UUID userId, UUID channelId) {
        ReadStatus readStatus = readStatusRepository.findById(userId, channelId)
                .orElseThrow(() -> new NoSuchElementException("ReadStatus not found for user "
                        + userId + " and channel " + channelId));

        readStatusRepository.deleteByUserIdAndChannelId(userId, channelId);
    }
}