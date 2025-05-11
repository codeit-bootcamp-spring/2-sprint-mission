package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.readStatus.DuplicateReadStatusException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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

    User user = userRepository.findById(userId)
        .orElseThrow(
            () -> {
              log.warn("readStatus 생성 실패 - 존재하지않는 유저 ID: {}", userId);
              Map<String, Object> details = new HashMap<>();
              details.put("userId", userId);
              return new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND, details);
            });
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> {
              log.warn("readStatus 생성 실패 - 존재하지않는 채널 ID: {}", channelId);
              Map<String, Object> details = new HashMap<>();
              details.put("channelId", channelId);
              return new ChannelNotFoundException(Instant.now(), ErrorCode.CHANNEL_NOT_FOUND, details);
            }
        );

    if (readStatusRepository.existsByUserIdAndChannelId(user.getId(), channel.getId())) {
      log.warn("readStatus 생성 실패 - 이미 존재하는 readStatus - userId: {}, channelId: {}", userId, channelId);
      Map<String, Object> details = new HashMap<>();
      details.put("userId", userId);
      details.put("channelId", channelId);
      throw new DuplicateReadStatusException(Instant.now(), ErrorCode.DUPLICATE_READSTATUS, details);
    }

    Instant lastReadAt = request.lastReadAt();
    ReadStatus readStatus = new ReadStatus(user, channel, lastReadAt);
    readStatusRepository.save(readStatus);

    return readStatusMapper.toDto(readStatus);
  }

  @Override
  public ReadStatusDto find(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId)
        .map(readStatusMapper::toDto)
        .orElseThrow(
            () -> {
              log.warn("readStatus 찾기 실패 - readStatusId: {}", readStatusId);
              Map<String, Object> details = new HashMap<>();
              details.put("readStatusId", readStatusId);
              return new ReadStatusNotFoundException(Instant.now(), ErrorCode.READSTATUS_NOT_FOUND, details);
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
    Instant newLastReadAt = request.newLastReadAt();
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> {
              log.warn("readStatus 수정 실패 - readStatus를 찾을 수 없음 - readStatusId: {}", readStatusId);
              Map<String, Object> details = new HashMap<>();
              details.put("readStatusId", readStatusId);
              return new ReadStatusNotFoundException(Instant.now(), ErrorCode.READSTATUS_NOT_FOUND, details);
            });
    readStatus.update(newLastReadAt);
    return readStatusMapper.toDto(readStatus);
  }

  @Transactional
  @Override
  public void delete(UUID readStatusId) {
    if (!readStatusRepository.existsById(readStatusId)) {
      log.warn("readStatus 삭제 실패 - readStatus를 찾을 수 없음 - readStatusId: {}", readStatusId);
      Map<String, Object> details = new HashMap<>();
      details.put("readStatusId", readStatusId);
      throw new ReadStatusNotFoundException(Instant.now(), ErrorCode.READSTATUS_NOT_FOUND, details);
    }
    readStatusRepository.deleteById(readStatusId);
  }
}
