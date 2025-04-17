package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.LogicException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ReadStatusMapper readStatusMapper;

    @Override
    public ReadStatusDto create(ReadStatusCreateDto readStatusCreateDto) {
        User user = userRepository.findById(readStatusCreateDto.userId())
                .orElseThrow(() -> new LogicException(ErrorCode.USER_NOT_FOUND));

        Channel channel = channelRepository.findById(readStatusCreateDto.channelId())
                .orElseThrow(() -> new LogicException(ErrorCode.CHANNEL_NOT_FOUND));

        if (readStatusRepository.existsByUserIdAndChannelId(readStatusCreateDto.userId(),
                readStatusCreateDto.channelId())) {
            throw new LogicException(ErrorCode.READ_STATUS_ALREADY_EXISTS);
        }

        ReadStatus newReadStatus = new ReadStatus(user, channel, readStatusCreateDto.lastReadAt());
        readStatusRepository.save(newReadStatus);

        return readStatusMapper.toDto(newReadStatus);
    }

    @Override
    public ReadStatusDto findById(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(() -> new LogicException(ErrorCode.READ_STATUS_NOT_FOUND));

        return readStatusMapper.toDto(readStatus);
    }

    @Override
    public List<ReadStatusDto> findAll() {
        return readStatusRepository.findAll().stream()
                .map(readStatusMapper::toDto)
                .toList();
    }

    @Override
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream()
                .map(readStatusMapper::toDto)
                .toList();
    }

    @Override
    public List<ReadStatusDto> findAllByChannelId(UUID channelId) {
        return readStatusRepository.findAllByChannelId(channelId).stream()
                .map(readStatusMapper::toDto)
                .toList();
    }

    @Override
    public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateDto readStatusUpdateDto) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new LogicException(ErrorCode.READ_STATUS_NOT_FOUND));
        readStatus.update(readStatusUpdateDto.newLastReadAt());

        return readStatusMapper.toDto(readStatus);
    }

    @Override
    public void delete(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(() -> new LogicException(ErrorCode.READ_STATUS_NOT_FOUND));

        readStatusRepository.delete(readStatus);
    }
}
