package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusAlreadyException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusMapper readStatusMapper;

    @Override
    public ReadStatusDto save(ReadStatusRequest readStatusRequest) {
        User user = userRepository.findById(readStatusRequest.userId())
            .orElseThrow(
                () -> UserNotFoundException.forId(readStatusRequest.userId().toString()));

        Channel channel = channelRepository.findById(readStatusRequest.channelId())
            .orElseThrow(
                () -> ChannelNotFoundException.forId(readStatusRequest.channelId().toString()));

        if (readStatusRepository.findByUserIdAndChannelId(
            readStatusRequest.userId(), readStatusRequest.channelId()).isPresent()) {
            throw ReadStatusAlreadyException.forUserIdAndChannelId(
                readStatusRequest.userId().toString(), readStatusRequest.channelId().toString());
        }

        ReadStatus readStatus = new ReadStatus(user, channel, readStatusRequest.lastReadAt());

        readStatusRepository.save(readStatus);
        return readStatusMapper.toDto(readStatus);
    }

    @Override
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findByUserId(userId).stream()
            .map(readStatusMapper::toDto)
            .toList();
    }

    @Override
    @Transactional
    public ReadStatusDto update(UUID readStatusId,
        ReadStatusUpdateRequest readStatusUpdateRequest) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
            .orElseThrow(() -> ReadStatusNotFoundException.forId(readStatusId.toString()));
        readStatus.updateLastReadAt(readStatusUpdateRequest.newLastReadAt());
        return readStatusMapper.toDto(readStatus);
    }
}
