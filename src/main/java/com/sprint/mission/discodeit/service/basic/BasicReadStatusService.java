package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.Locked.Read;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusMapper readStatusMapper;

  @Override
  public ReadStatusDto create(ReadStatusCreateRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new IllegalArgumentException("[Error] 해당 ID의 사용자가 존재하지 않습니다."));
    Channel channel = channelRepository.findById(request.channelId())
        .orElseThrow(() -> new IllegalArgumentException("[Error] 해당 ID의 채널이 존재하지 않습니다."));

    if (readStatusRepository.existsByUserAndChannel((user), channel)) {
      throw new IllegalArgumentException(
          "해당 사용자와 채널에 대한 ReadStatus가 이미 존재합니다. userId: " + request.userId() + ", channelId: "
              + request.channelId());
    }

    ReadStatus readStatus = readStatusMapper.toEntity(request, user, channel);
    ReadStatus savedReadStatus = readStatusRepository.save(readStatus);

    return readStatusMapper.toDto(savedReadStatus);
  }

  @Transactional(readOnly = true)
  @Override
  public ReadStatusDto find(UUID id) {
    ReadStatus readStatus = readStatusRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("[Error] ReadStatus null"));

    return readStatusMapper.toDto(readStatus);
  }

  @Transactional(readOnly = true)
  @Override
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("[Error] 해당 ID의 사용자가 존재하지 않습니다."));

    List<ReadStatus> readStatuses = readStatusRepository.findAllByUser((user));
    if (readStatuses == null || readStatuses.isEmpty()) {
      throw new IllegalArgumentException("[Error] 해당 사용자에 대한 ReadStatus가 존재하지 않습니다.");
    }

    return readStatuses.stream()
        .map(readStatusMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public ReadStatusDto update(UUID id, ReadStatusUpdateRequest request) {
    ReadStatus readStatus = readStatusRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("[Error] 해당 ID의 ReadStatus가 존재하지 않습니다."));

    readStatus.setLastReadAt(request.newLastReadAt());
    ReadStatus updatedReadStatus = readStatusRepository.save(readStatus);
    return readStatusMapper.toDto(updatedReadStatus);
  }

  @Transactional
  @Override
  public void delete(UUID id) {
    ReadStatus readStatus = readStatusRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("[Error] 해당 ID의 ReadStatus가 존재하지 않습니다."));
    readStatusRepository.delete(readStatus);
  }
}
