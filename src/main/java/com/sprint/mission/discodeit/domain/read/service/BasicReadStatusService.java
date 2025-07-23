package com.sprint.mission.discodeit.domain.read.service;

import com.sprint.mission.discodeit.domain.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.read.DuplicateReadStatusException;
import com.sprint.mission.discodeit.domain.read.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.domain.read.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.domain.read.dto.ReadStatusDto;
import com.sprint.mission.discodeit.domain.read.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.domain.read.entity.ReadStatus;
import com.sprint.mission.discodeit.domain.read.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.domain.user.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @PreAuthorize("principal.userDto.id == #request.userId()")
  @Transactional
  @Override
  public ReadStatusDto create(ReadStatusCreateRequest request) {
    log.debug("읽음 상태 생성 시작: userId={}, channelId={}", request.userId(), request.channelId());

    UUID userId = request.userId();
    UUID channelId = request.channelId();

    User user = userRepository.findById(userId)
        .orElseThrow(() -> UserNotFoundException.withId(userId));
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> ChannelNotFoundException.withId(channelId));

    if (readStatusRepository.existsByUserIdAndChannelId(user.getId(), channel.getId())) {
      throw DuplicateReadStatusException.withUserIdAndChannelId(userId, channelId);
    }

    Instant lastReadAt = request.lastReadAt();
    ReadStatus readStatus = new ReadStatus(user, channel, lastReadAt);
    readStatusRepository.save(readStatus);

    log.info("읽음 상태 생성 완료: id={}, userId={}, channelId={}",
        readStatus.getId(), userId, channelId);
    return ReadStatusDto.from(readStatus);
  }

  @Override
  public ReadStatusDto find(UUID readStatusId) {
    log.debug("읽음 상태 조회 시작: id={}", readStatusId);
    ReadStatusDto dto = readStatusRepository.findById(readStatusId)
        .map(ReadStatusDto::from)
        .orElseThrow(() -> ReadStatusNotFoundException.withId(readStatusId));
    log.info("읽음 상태 조회 완료: id={}", readStatusId);
    return dto;
  }

  @Override
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    log.debug("사용자별 읽음 상태 목록 조회 시작: userId={}", userId);
    List<ReadStatusDto> dtos = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatusDto::from)
        .toList();
    log.info("사용자별 읽음 상태 목록 조회 완료: userId={}, 조회된 항목 수={}", userId, dtos.size());
    return dtos;
  }

  @PostAuthorize("principal.userDto.id == returnObject.userId()")
  @Transactional
  @Override
  public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
    log.debug("읽음 상태 수정 시작: id={}, newLastReadAt={}, notificationEnabled={}",
        readStatusId, request.newLastReadAt(), request.newNotificationEnabled());

    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> ReadStatusNotFoundException.withId(readStatusId));
    readStatus.update(request.newLastReadAt(), request.newNotificationEnabled());

    log.info("읽음 상태 수정 완료: id={}, notificationEnabled={}",
        readStatusId, readStatus.isNotificationEnabled());
    return ReadStatusDto.from(readStatus);
  }

  @Transactional
  @Override
  public void delete(UUID readStatusId) {
    log.debug("읽음 상태 삭제 시작: id={}", readStatusId);
    if (!readStatusRepository.existsById(readStatusId)) {
      throw ReadStatusNotFoundException.withId(readStatusId);
    }
    readStatusRepository.deleteById(readStatusId);
    log.info("읽음 상태 삭제 완료: id={}", readStatusId);
  }
}
