package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.Mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.response.ReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFound;
import com.sprint.mission.discodeit.exception.readstatus.DuplicateReadStatus;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFound;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusMapper readStatusMapper;

  @Override
  public ReadStatusDto create(ReadStatusCreateRequest request) {
    UUID userId = request.userId();
    UUID channelId = request.channelId();
    var lastReadAt = request.lastReadAt();
    log.info("ReadStatus 생성 요청: userId={}, channelId={}, lastReadAt={}", userId, channelId, lastReadAt);
    if (!userRepository.existsById(userId)) {
      log.warn("ReadStatus 생성 실패 (user 없음): userId={}", userId);
      throw new ReadStatusNotFound(Map.of("userId",userId));
    }
    if (!channelRepository.existsById(channelId)) {
      log.warn("ReadStatus 생성 실패 (channel 없음): channelId={}", channelId);
      throw new ChannelNotFound(Map.of("channelId",channelId));
    }
    boolean exists = readStatusRepository.findAllByUserId(userId).stream()
            .anyMatch(rs -> rs.getChannel().getId().equals(channelId));
    if (exists) {
      log.warn("ReadStatus 생성 실패 (이미 존재): userId={}, channelId={}", userId, channelId);
      throw new DuplicateReadStatus(Map.of("userId",userId, "channelId",channelId));
    }
    User user = userRepository.findById(userId).orElse(null);
    Channel channel = channelRepository.findById(channelId).orElse(null);
    ReadStatus readStatus = new ReadStatus(user, channel, lastReadAt);
    ReadStatus saved = readStatusRepository.save(readStatus);
    ReadStatusDto dto = readStatusMapper.toDto(saved);
    log.info("ReadStatus 생성 완료: id={}, userId={}, channelId={}", saved.getId(), userId, channelId);
    return dto;
  }

  @Override
  public ReadStatusDto find(UUID readStatusId) {
    log.info("ReadStatus 조회 요청: id={}", readStatusId);
    ReadStatusDto dto = readStatusRepository.findById(readStatusId)
            .map(readStatusMapper::toDto)
            .orElseThrow(() -> {
              log.warn("ReadStatus 조회 실패: id={} not found", readStatusId);
              return new ReadStatusNotFound(Map.of("readStatusId", readStatusId));
            });
    log.info("ReadStatus 조회 완료: id={}", readStatusId);
    return dto;
  }

  @Override
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    log.info("사용자 ReadStatus 목록 조회 요청: userId={}", userId);
    List<ReadStatusDto> list = readStatusRepository.findAllByUserId(userId).stream()
            .map(readStatusMapper::toDto)
            .toList();
    log.info("사용자 ReadStatus 목록 조회 완료: userId={}, count={}", userId, list.size());
    return list;
  }

  @Override
  public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
    var newLastReadAt = request.newLastReadAt();
    log.info("ReadStatus 수정 요청: id={}, newLastReadAt={}", readStatusId, newLastReadAt);
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
            .orElseThrow(() -> {
              log.warn("ReadStatus 수정 실패: id={} not found", readStatusId);
              return new ReadStatusNotFound(Map.of("readStatusId", readStatusId));
            });
    readStatus.update(newLastReadAt);
    ReadStatusDto dto = readStatusMapper.toDto(readStatusRepository.save(readStatus));
    log.info("ReadStatus 수정 완료: id={}, lastReadAt={}", readStatusId, newLastReadAt);
    return dto;
  }

  @Override
  public void delete(UUID readStatusId) {
    log.info("ReadStatus 삭제 요청: id={}", readStatusId);
    if (!readStatusRepository.existsById(readStatusId)) {
      log.warn("ReadStatus 삭제 실패: id={} not found", readStatusId);
      throw new ReadStatusNotFound(Map.of("readStatusId", readStatusId));
    }
    readStatusRepository.deleteById(readStatusId);
    log.info("ReadStatus 삭제 완료: id={}", readStatusId);
  }
}