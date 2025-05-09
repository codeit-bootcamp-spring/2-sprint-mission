package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusAlreadyExistsException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusMapper readStatusMapper;

    @Override
    public ReadStatusDto create(ReadStatusCreateRequest request) {
        UUID userId = request.userId();
        UUID channelId = request.channelId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ChannelNotFoundException(channelId));

        if (readStatusRepository.findAllByUser(user).stream()
                .anyMatch(readStatus -> readStatus.getChannel().getId().equals(channelId))) {
            throw new ReadStatusAlreadyExistsException(userId, channelId);
        }

        ReadStatus readStatus = ReadStatus.builder()
                .user(user)
                .channel(channel)
                .lastReadAt(request.lastReadAt())
                .build();

        ReadStatus savedReadStatus = readStatusRepository.save(readStatus);
        return readStatusMapper.toDto(savedReadStatus);
    }

    @Override
    public ReadStatusDto find(UUID readStatusId) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new ReadStatusNotFoundException(readStatusId));
        return readStatusMapper.toDto(readStatus);
    }

    @Override
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        List<ReadStatus> readStatuses = readStatusRepository.findAllByUser_Id(userId);
        return readStatusMapper.toDtoList(readStatuses);
    }

    @Override
    public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
        Instant newLastReadAt = request.newLastReadAt();
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new ReadStatusNotFoundException(readStatusId));
        readStatus.updateLastReadAt(newLastReadAt);

        ReadStatus updatedReadStatus = readStatusRepository.save(readStatus);
        return readStatusMapper.toDto(updatedReadStatus);
    }

    @Override
    public void delete(UUID readStatusId) {
        if (!readStatusRepository.existsById(readStatusId)) {
            throw new ReadStatusNotFoundException(readStatusId);
        }
        readStatusRepository.deleteById(readStatusId);
    }

    @Override
    public List<ReadStatusDto> findAllByUser(User user) {
        List<ReadStatus> readStatuses = readStatusRepository.findAllByUser(user);
        return readStatusMapper.toDtoList(readStatuses);
    }

    @Override
    public List<ReadStatusDto> findAllByChannel(Channel channel) {
        List<ReadStatus> readStatuses = readStatusRepository.findAllByChannel(channel);
        return readStatusMapper.toDtoList(readStatuses);
    }

    @Override
    public void deleteAllByChannel(Channel channel) {
        readStatusRepository.deleteAllByChannel(channel);
    }
}