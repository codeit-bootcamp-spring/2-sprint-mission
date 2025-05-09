package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.service.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.service.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusAlreadyExsistsException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicReadStatusService implements ReadStatusService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final ReadStatusMapper readStatusMapper;

  @Override
  @Transactional
  public ReadStatusDto create(ReadStatusCreateRequest request) {
    log.debug("메시지 수신 정보 생성 시작: {}", request);
    UUID userId = request.userId();
    UUID channelId = request.channelId();

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException().notFoundWithId(userId));
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException().notFoundWithId(channelId));

    if (readStatusRepository.existsByUserIdAndChannelId(userId, channelId)) {
      throw new ReadStatusAlreadyExsistsException().duplicateUserIdAndChannelId(userId, channelId);
    }

    ReadStatus status = new ReadStatus(user, channel, request.lastReadAt());
    readStatusRepository.save(status);
    log.info("메시지 수신 정보 생성 완료: id={}, userId={}, channelId={}", status.getId(), userId, channelId);
    return readStatusMapper.toDto(status);
  }

  @Override
  @Transactional(readOnly=true)
  public ReadStatus find(UUID id) {
    return readStatusRepository.findById(id)
        .orElseThrow(() -> new ReadStatusNotFoundException().notFoundWithId(id));
  }

  @Override
  @Transactional(readOnly=true)
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new UserNotFoundException().notFoundWithId(userId);
    }
    List<ReadStatus> statusList = readStatusRepository.findAllByUserId(userId);
    return readStatusMapper.toDtoList(statusList);
  }

  @Override
  public List<UUID> findAllChannelIdByUserId(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new UserNotFoundException().notFoundWithId(userId);
    }
    return readStatusRepository.findAllChannelIdByUserId(userId);
  }

  @Override
  @Transactional
  public ReadStatusDto update(UUID id, ReadStatusUpdateRequest request) {
    log.debug("메시지 수신 정보 수정 시작: id={}, request={}", id, request);
    ReadStatus status = find(id);
    status.update(request.newLastReadAt());
    log.info("메시지 수신 정보 수정 완료: id={}", id);
    return readStatusMapper.toDto(status);
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    log.debug("메시지 수신 정보 삭제 시작: id={}", id);
    if (!readStatusRepository.existsById(id)) {
      throw new ReadStatusNotFoundException().notFoundWithId(id);
    }
    readStatusRepository.deleteById(id);
    log.info("메시지 수신 정보 삭제 완료: id={}", id);
  }
}
