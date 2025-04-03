package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.controller.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.controller.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity._ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  public _ReadStatus create(ReadStatusCreateRequest request) {
    UUID userId = UUID.fromString(request.getUserId().toString());
    UUID getChannelId = UUID.fromString(request.getChannelId().toString());

    if (!userRepository.existsById(userId)) {
      throw new NoSuchElementException("User with id " + userId + " does not exist");
    }
    if (!channelRepository.existsById(getChannelId)) {
      throw new NoSuchElementException("Channel with id " + getChannelId + " does not exist");
    }
    if (readStatusRepository.findAllByUserId(userId).stream()
        .anyMatch(readStatus -> readStatus.getGetChannelId().equals(getChannelId))) {
      throw new IllegalArgumentException(
          "ReadStatus with userId " + userId + " and getChannelId " + getChannelId
              + " already exists");
    }

    OffsetDateTime lastReadAt = parse(request.getLastReadAt().toString());

    _ReadStatus readStatus = new _ReadStatus(userId, getChannelId, lastReadAt);
    return readStatusRepository.save(readStatus);
  }

  @Override
  public _ReadStatus find(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("ReadStatus with id " + readStatusId + " not found"));
  }

  @Override
  public List<_ReadStatus> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId).stream()
        .toList();
  }

  @Override
  public _ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest request) {
    OffsetDateTime newLastReadAt = parse(request.getNewLastReadAt().toString());
    _ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("ReadStatus with id " + readStatusId + " not found"));
    readStatus.update(newLastReadAt);
    return readStatusRepository.save(readStatus);
  }

  @Override
  public void delete(UUID readStatusId) {
    if (!readStatusRepository.existsById(readStatusId)) {
      throw new NoSuchElementException("ReadStatus with id " + readStatusId + " not found");
    }
    readStatusRepository.deleteById(readStatusId);
  }

  private OffsetDateTime parse(String date) {
    LocalDateTime parse = LocalDateTime.parse(date,
        DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));  // Stirng to Instant
    Instant instant = parse.atZone(ZoneId.systemDefault()).toInstant();
    return OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
  }
}
