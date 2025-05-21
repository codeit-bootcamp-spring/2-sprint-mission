package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ChannelException;
import com.sprint.mission.discodeit.exception.ReadStatusException;
import com.sprint.mission.discodeit.exception.UserException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusMapper readStatusMapper;

  @Transactional
  @Override
  public ReadStatusDto create(ReadStatusCreateRequest request) {
    UUID userId = request.userId();
    UUID channelId = request.channelId();

    log.info("Attempting to create ReadStatus for userId {} and channelId {}", userId, channelId);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.error("User with id {} does not exist", userId);
          return UserException.userNotFound(Map.of("userId", userId));
        });
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> {
          log.error("Channel with id {} does not exist", channelId);
          return ChannelException.channelNotFound(Map.of("channelId", channelId));
        });

    if (readStatusRepository.existsByUserIdAndChannelId(user.getId(), channel.getId())) {
      log.error("ReadStatus with userId {} and channelId {} already exists", userId, channelId);
      throw ReadStatusException.readStatusAlreadyExist(
          Map.of("userId", userId, "channelId", channelId));
    }

    Instant lastReadAt = request.lastReadAt();
    ReadStatus readStatus = new ReadStatus(user, channel, lastReadAt);
    readStatusRepository.save(readStatus);

    log.info("ReadStatus for userId {} and channelId {} successfully created", userId, channelId);
    return readStatusMapper.toDto(readStatus);
  }

  @Override
  public ReadStatusDto find(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId)
        .map(readStatusMapper::toDto)
        .orElseThrow(() -> {
          log.error("ReadStatus with id {} not found", readStatusId);
          return ReadStatusException.readStatusNotFound(Map.of("readStatusId", readStatusId));
        });
  }

  @Override
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatusMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
    log.info("Attempting to update ReadStatus with id {}", readStatusId);

    Instant newLastReadAt = request.newLastReadAt();
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> {
          log.error("ReadStatus with id {} not found", readStatusId);
          return ReadStatusException.readStatusNotFound(Map.of("readStatusId", readStatusId));
        });

    readStatus.update(newLastReadAt);

    log.info("ReadStatus with id {} successfully updated", readStatusId);
    return readStatusMapper.toDto(readStatus);
  }

  @Transactional
  @Override
  public void delete(UUID readStatusId) {
    log.info("Attempting to delete ReadStatus with id {}", readStatusId);

    if (!readStatusRepository.existsById(readStatusId)) {
      log.error("ReadStatus with id {} not found", readStatusId);
      throw ReadStatusException.readStatusNotFound(Map.of("readStatusId", readStatusId));
    }

    readStatusRepository.deleteById(readStatusId);
    log.info("Successfully deleted ReadStatus with id {}", readStatusId);
  }
}
